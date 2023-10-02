package com.example.springBootDemo.service.impl;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.example.springBootDemo.config.components.constant.StockConstant;
import com.example.springBootDemo.config.components.system.SystemConfConstant;
import com.example.springBootDemo.dao.mapper.ConfBusinessDao;
import com.example.springBootDemo.entity.ConfCxStock;
import com.example.springBootDemo.entity.ConfMyStock;
import com.example.springBootDemo.entity.input.ConfBusiness;
import com.example.springBootDemo.service.ConfBusinessService;
import com.example.springBootDemo.service.ConfMyStockService;
import com.example.springBootDemo.util.excel.ExcelChangeUtil;
import com.example.springBootDemo.util.excel.ExcelUtil;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.*;
import java.util.stream.Collectors;


/**
 * 行业配置化(ConfBusiness)表服务实现类
 *
 * @author makejava
 * @since 2023-09-13 16:37:28
 */
@Slf4j
@Service("confBusinessService")
public class ConfBusinessServiceImpl extends ServiceImpl<ConfBusinessDao, ConfBusiness> implements ConfBusinessService {
    @Resource
    private ConfBusinessDao confBusinessDao;
    @Resource
    private ConfBusinessService confBusinessService;
    @Resource
    private ConfMyStockService confMyStockService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean imporConfBusiness(MultipartFile mf) throws Exception {
        //全量配置
        EntityWrapper<ConfBusiness> wrapper = new EntityWrapper<>();
        confBusinessService.delete(wrapper);

        List<ConfBusiness> cbList = ExcelUtil.importExcel(0, 1, mf, ConfBusiness.class);

        EntityWrapper<ConfMyStock> cmsWrapper = new EntityWrapper<>();
        List<ConfMyStock> mySotckList = confMyStockService.selectList(cmsWrapper);
        Map<String, String> map = mySotckList.stream().collect(Collectors.toMap(ConfMyStock::getStockName, ConfMyStock::getStockCode, (item1, item2) -> item1));

        //code和创建时间要不要加一个 2023-9-14
        for (ConfBusiness cb : cbList) {
            cb.setCreateDate(new Date());

            String codeCoreList = cb.getCodeCoreList();
            String codeList = cb.getCodeList();

            String listStr = cb.getList();
            String coreListStr = cb.getCoreList();

            //标的池中去掉核心标的
            if (StringUtils.isNotBlank(coreListStr) && StringUtils.isNotBlank(listStr)) {
                List<String> removeList = Lists.newArrayList(coreListStr.split(","));
                List<String> list = Lists.newArrayList(listStr.split(","));
                list.removeAll(removeList);
                listStr = list.stream().collect(Collectors.joining(","));
                log.info("概念:{}list size:{}", cb.getBusName(), listStr.length());
            }
            String instructions = cb.getInstructions();
            if (StringUtils.isNotBlank(instructions) && instructions.length() > 255) {
                log.info("概念:{} 说明 size:{}", cb.getBusName(), instructions.length());
            }

            if (StringUtils.isNotBlank(coreListStr)
                    && (StringUtils.isBlank(codeCoreList) || codeCoreList.split(",").length != coreListStr.split(",").length)) {
                cb.setCodeCoreList(getCode(map, coreListStr));
            }
            if (StringUtils.isNotBlank(listStr)
                    && (StringUtils.isBlank(codeList) || codeList.split(",").length != listStr.split(",").length)) {
                cb.setCodeList(getCode(map, listStr));
            }


        }

        return confBusinessService.insertBatch(cbList, cbList.size());
    }

    public String getCode(Map<String, String> map, String strList) {
        if (StringUtils.isBlank(strList)) {
            return "";
        }
        List<String> resultList = Lists.newArrayList();
        List<String> tempList = Lists.newArrayList(strList.split(","));

        for (String str : tempList) {
            String value = map.get(str);
            resultList.add(value);
        }
        return resultList.stream().collect(Collectors.joining(","));
    }

    @Override
    public void exportConfBusiness(HttpServletResponse response, List<ConfBusiness> addList) throws Exception {
        EntityWrapper<ConfBusiness> wrapper = new EntityWrapper<>();
        wrapper.eq("state", 1);
        List<ConfBusiness> list = confBusinessDao.selectList(wrapper);
        list.addAll(addList);
        list = list.stream()
                .sorted(Comparator.comparing(ConfBusiness::getType, Comparator.nullsFirst(String::compareTo)))
                .sorted(Comparator.comparing(ConfBusiness::getSort, Comparator.nullsFirst(String::compareTo)))
                .collect(Collectors.toList());
        //生成excel文档
        ExportParams params = ExcelUtil.getSimpleExportParams(list, ConfBusiness.class);
        Workbook workbook = ExcelExportUtil.exportExcel(params, ConfBusiness.class, list);
        ExcelUtil.exportExel(response, "ConfBusinessExcel", workbook);
    }


    @Override
    public void refushConfBusiness() {
        //全量配置
        EntityWrapper<ConfBusiness> wrapper = new EntityWrapper<>();
        //刷新标记,0不刷新,1需要刷新
        wrapper.eq("refush_flag", 1);
        List<ConfBusiness> list = confBusinessService.selectList(wrapper);

        log.info("共有{}条概念需要处理", list.size());
        for (int i = 0; i < list.size(); i++) {
            ConfBusiness cb = list.get(i);
            refushRecordConfBusiness(cb);
        }

    }

    /**
     * 根据一条配置信息进行刷新
     *
     * @param cb
     */
    public void refushRecordConfBusiness(ConfBusiness cb) {
        String busName = cb.getBusName();
        log.info("开始处理概念:[{}]", busName);
        String alias = cb.getAlias();
        String type = cb.getType();
        if (StringUtils.isBlank(alias)) {
            alias = "";
        }

        if (StockConstant.BusinessTypeEnum.ATTR.getName().equals(type)) {
            //1 检索数据库中已有概念的标的，并将其中的概念抹去
            clearAttr(cb, busName, alias);
            clearBusiness(cb, busName, alias);
            //2 搜索配置中的标的，并将对应的概念加上，然后落库
            //3.修改库中相关概念
            queryAndUpdateAttr(cb, busName);
        }
        if (StockConstant.BusinessTypeEnum.FLAG.getName().equals(type)) {
            log.info("标记数据:[{}]不做处理:", busName);
        } else {
            //1 检索数据库中已有概念的标的，并将其中的概念抹去
            clearBusiness(cb, busName, alias);
            //2 搜索配置中的标的，并将对应的概念加上，然后落库
            //3.修改库中相关概念
            queryAndUpdateBusiness(cb, busName);
        }

        //4.修改配置表信息
        log.info("概念:[{}]修改刷新标记", cb.getBusName());
        cb.setRefushFlag("0");
        confBusinessService.updateById(cb);
        log.info("处理成功");
    }

    private void queryAndUpdateAttr(ConfBusiness cb, String busName) {
        //2 搜索配置中的标的，并将对应的概念加上，然后落库
        List<String> codeList = Lists.newArrayList();
        String coreCode = cb.getCodeCoreList();
        String code = cb.getCodeList();
        List<String> ccList = Lists.newArrayList();
        List<String> cList = Lists.newArrayList();

        if (StringUtils.isNotBlank(coreCode)) {
            ccList = Lists.newArrayList(coreCode.split(","));
            codeList.addAll(ccList);
        }
        if (StringUtils.isNotBlank(code)) {
            cList = Lists.newArrayList(code.split(","));
            codeList.addAll(cList);
        }

        if (CollectionUtils.isEmpty(codeList)) {
            log.info("busName:{}没有需要处理的数据", busName);
            return;
        }

        EntityWrapper<ConfMyStock> cmsWrapper2 = new EntityWrapper<>();
        cmsWrapper2.in("STOCK_CODE", codeList);
        List<ConfMyStock> mySotckList2 = confMyStockService.selectList(cmsWrapper2);

        //3.修改库中相关概念
        for (ConfMyStock stock : mySotckList2) {
            String attr = stock.getAttr();
            attr = addEleBusiness(busName, attr);
            stock.setAttr(attr);

            stock.setModifedBy("增量概念刷新-修改概念");
            stock.setModifedDate(new Date());
        }
        log.info("概念:[{}]修改库中相关概念:[{}]", cb.getBusName(), mySotckList2);
        if (CollectionUtils.isNotEmpty(mySotckList2)) {
            confMyStockService.insertOrUpdateBatch(mySotckList2, mySotckList2.size());
        }
    }


    public void queryAndUpdateBusiness(ConfBusiness cb, String busName) {
        //2 搜索配置中的标的，并将对应的概念加上，然后落库
        List<String> codeList = Lists.newArrayList();
        String coreCode = cb.getCodeCoreList();
        String code = cb.getCodeList();
        List<String> ccList = Lists.newArrayList();
        List<String> cList = Lists.newArrayList();

        if (StringUtils.isNotBlank(coreCode)) {
            ccList = Lists.newArrayList(coreCode.split(","));
            codeList.addAll(ccList);
        }
        if (StringUtils.isNotBlank(code)) {
            cList = Lists.newArrayList(code.split(","));
            codeList.addAll(cList);
        }

        if (CollectionUtils.isEmpty(codeList)) {
            log.info("busName:{}没有需要处理的数据", busName);
            return;
        }

        EntityWrapper<ConfMyStock> cmsWrapper2 = new EntityWrapper<>();
        cmsWrapper2.in("STOCK_CODE", codeList);
        List<ConfMyStock> mySotckList2 = confMyStockService.selectList(cmsWrapper2);

        //3.修改库中相关概念
        for (ConfMyStock stock : mySotckList2) {
            String stockCode = stock.getStockCode();
            //主要名称设置
            if (ccList.contains(stockCode)) {
                String business = stock.getMainBusiness();
                String result = addEleBusiness(busName, business);
                stock.setMainBusiness(result);
            } else {
                String business = stock.getNicheBusiness();
                String result = addEleBusiness(busName, business);
                stock.setNicheBusiness(result);
            }

            //关联板块设置
            String relBus = cb.getRelBus();
            String business = stock.getMainBusiness();
            business = addBusiness(relBus, business);
            stock.setMainBusiness(business);

            //别名设置
            String nicheBusiness = stock.getNicheBusiness();
            String alias = cb.getAlias();
            nicheBusiness = addBusiness(alias, nicheBusiness);
            nicheBusiness = removeBusiness(business, nicheBusiness);
            stock.setNicheBusiness(nicheBusiness);

            stock.setModifedBy("增量概念刷新-修改概念");
            stock.setModifedDate(new Date());
        }
        log.info("概念:[{}]修改库中相关概念:[{}]", cb.getBusName(), mySotckList2);
        if (CollectionUtils.isNotEmpty(mySotckList2)) {
            confMyStockService.insertOrUpdateBatch(mySotckList2, mySotckList2.size());
        }
    }

    private void clearAttr(ConfBusiness cb, String busName, String alias) {
        List<String> attrList = Lists.newArrayList(alias.split(","));
        attrList.add(busName);
        EntityWrapper<ConfMyStock> cmsWrapper = new EntityWrapper<>();
        cmsWrapper.in("attr", attrList);
        List<ConfMyStock> mySotckList = confMyStockService.selectList(cmsWrapper);

        for (ConfMyStock stock : mySotckList) {
            String attr = stock.getAttr();

            for (String str : attrList) {
                attr = removeEleBusiness(str, attr);
            }

            stock.setAttr(attr);
            stock.setModifedBy("增量概念刷新-抹掉概念");
            stock.setModifedDate(new Date());
        }
        log.info("概念:[{}]查询已有概念的标的，并将其中的概念抹去：{}", cb.getBusName(), mySotckList);
        if (CollectionUtils.isNotEmpty(mySotckList)) {
            confMyStockService.updateBatchById(mySotckList, mySotckList.size());
        }

    }

    public void clearBusiness(ConfBusiness cb, String busName, String alias) {
        List<String> businessList = Lists.newArrayList(alias.split(","));
        businessList.add(busName);
        //注意这里没有删除关联板块属性
        EntityWrapper<ConfMyStock> cmsWrapper = new EntityWrapper<>();
        for (String str : businessList) {
            cmsWrapper.like("MAIN_BUSINESS", str).or().like("NICHE_BUSINESS", str);
        }
        List<ConfMyStock> mySotckList = confMyStockService.selectList(cmsWrapper);

        for (ConfMyStock stock : mySotckList) {
            String mb = stock.getMainBusiness();
            String nb = stock.getNicheBusiness();

            String mbResult = mb;
            String nbResult = nb;
            for (String str : businessList) {
                mbResult = removeEleBusiness(str, mbResult);
                nbResult = removeEleBusiness(str, nbResult);
            }

            stock.setMainBusiness(mbResult);
            stock.setNicheBusiness(nbResult);
            stock.setModifedBy("增量概念刷新-抹掉概念");
            stock.setModifedDate(new Date());
        }
        log.info("概念:[{}]查询已有概念的标的，并将其中的概念抹去：{}", cb.getBusName(), mySotckList);
        if (CollectionUtils.isNotEmpty(mySotckList)) {
            confMyStockService.updateBatchById(mySotckList, mySotckList.size());
        }
    }


    @Override
    public void exportConfBusinessWithTHS(HttpServletResponse response) throws Exception {
        ArrayList<ConfBusiness> thsList = Lists.newArrayList();
        String thsBasePath = SystemConfConstant.THS_BASE_PATH;

        //依次读取概念文件
        for (int i = 1; ; i++) {
            File file = new File(thsBasePath + "\\plate\\" + i + ".xls");
            if (file.exists() == false) {
                log.info("概念文件:[{}]未查询到数据，终止查询", file.getName());
                break;
            }
            File tempFile = ExcelChangeUtil.csvToXlsxConverter(file, file.getName());
            List<ConfCxStock> confList = ExcelUtil.excelToList(tempFile, ConfCxStock.class);
            log.info("概念文件:[{}]共：{}条概念", file.getName(), confList.size());

            //开始收集明细文件
            for (ConfCxStock ccs : confList) {
                File file2 = new File(thsBasePath + "\\plate\\" + ccs.getStockCode() + ".xls");
                if (file2.exists() == false) {
                    log.info("概念:{}-明细文件:[{}] 未查询到数据", ccs.getStockName(), file2.getName());
                    continue;
                }
                File tempFile2 = ExcelChangeUtil.csvToXlsxConverter(file2, file2.getName());
                List<ConfCxStock> stockList = ExcelUtil.excelToList(tempFile2, ConfCxStock.class);
                log.info("概念:{}-明细文件:[{}] 开始处理，共{}条明细", ccs.getStockName(), tempFile2.getName(), stockList.size());

                String codeList = stockList.stream().map(ConfCxStock::getStockCode).collect(Collectors.joining(","));
                String nameList = stockList.stream().map(ConfCxStock::getStockName).collect(Collectors.joining(","));
                ConfBusiness c = ConfBusiness.builder()
                        .sort(ccs.getStockCode())
                        .refushFlag("0")
                        .busName(ccs.getStockName())
                        .list(nameList)
                        .codeList(codeList)
                        .build();

                thsList.add(c);
            }
        }

        exportConfBusiness(response, thsList);
    }

    @Override
    public List<ConfBusiness> queryPlateCore() {
        return confBusinessDao.queryPlateCore();
    }

    @Override
    public List<ConfBusiness> getAliasRelation() {
        return confBusinessDao.getAliasRelation();
    }

    private String addEleBusiness(String ele, String business) {
        List<String> list = Lists.newArrayList();
        if (StringUtils.isNotBlank(business)) {
            list = Lists.newArrayList(business.split(","));
        }
        if (StringUtils.isNotBlank(ele)) {
            list.add(ele);
        }
        list.remove("");
        return list.stream().distinct().collect(Collectors.joining(","));
    }

    private String addBusiness(String eles, String business) {
        List<String> list = Lists.newArrayList();
        if (StringUtils.isNotBlank(business)) {
            list = Lists.newArrayList(business.split(","));
        }
        if (StringUtils.isNotBlank(eles)) {
            List<String> tempList = Lists.newArrayList(eles.split(","));
            list.addAll(tempList);
        }
        list.remove("");
        return list.stream().distinct().collect(Collectors.joining(","));
    }


    private String removeEleBusiness(String ele, String business) {
        if (StringUtils.isBlank(business)) {
            return "";
        }
        List<String> list = Lists.newArrayList(business.split(","));
        list.remove(ele);
        list.remove("");
        return list.stream().distinct().collect(Collectors.joining(","));
    }

    private String removeBusiness(String eles, String business) {
        if (StringUtils.isBlank(business)) {
            return "";
        }
        List<String> list = Lists.newArrayList(business.split(","));
        if (StringUtils.isNotBlank(eles)) {
            List<String> tempList = Lists.newArrayList(eles.split(","));
            list.removeAll(tempList);
        }
        list.remove("");
        return list.stream().distinct().collect(Collectors.joining(","));
    }
}
