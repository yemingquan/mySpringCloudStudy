package com.example.springBootDemo.service.impl;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.example.springBootDemo.dao.mapper.ConfBusinessDao;
import com.example.springBootDemo.entity.ConfMySotck;
import com.example.springBootDemo.entity.input.ConfBusiness;
import com.example.springBootDemo.service.ConfBusinessService;
import com.example.springBootDemo.service.ConfMySotckService;
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
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Map;
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
    private ConfMySotckService confMySotckService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean imporConfBusiness(MultipartFile mf) throws Exception {
        //全量配置
        EntityWrapper<ConfBusiness> wrapper = new EntityWrapper<>();
        confBusinessService.delete(wrapper);

        List<ConfBusiness> list = ExcelUtil.importExcel(mf, ConfBusiness.class);

        EntityWrapper<ConfMySotck> cmsWrapper = new EntityWrapper<>();
        List<ConfMySotck> mySotckList = confMySotckService.selectList(cmsWrapper);
        Map<String, String> map = mySotckList.stream().collect(Collectors.toMap(ConfMySotck::getStockName, ConfMySotck::getStockCode, (item1, item2) -> item1));

        //code和创建时间要不要加一个 2023-9-14
        for (ConfBusiness cb : list) {
            cb.setCreateDate(new Date());
            String strCList = cb.getCoreList();
            String strList = cb.getList();

            cb.setCodeCoreList(getCode(map, strCList));
            cb.setCodeList(getCode(map, strList));
        }

        return confBusinessService.insertBatch(list, list.size());
    }

    public String getCode(Map<String, String> map, String strCList) {
        if (StringUtils.isBlank(strCList)) {
            return "";
        }
        List<String> resultList = Lists.newArrayList();
        List<String> tempList = Lists.newArrayList(strCList.split(","));
        for (String str : tempList) {
            String value = map.get(str);
            resultList.add(value);
        }
        return resultList.stream().collect(Collectors.joining(","));
    }

    @Override
    public void exportConfBusiness(HttpServletResponse response) throws Exception {
        String fileName = "ConfBusinessExcel.xls";
        EntityWrapper<ConfBusiness> wrapper = new EntityWrapper<>();
        List<ConfBusiness> list = confBusinessDao.selectList(wrapper);
        //生成excel文档
        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams("行业配置化", "sheet"),
                ConfBusiness.class, list);

        fileName = new String(fileName.getBytes("UTF-8"), StandardCharsets.ISO_8859_1);
        response.setContentType("application/octet-stream");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName);
        OutputStream outputStream = response.getOutputStream();
        response.flushBuffer();
        workbook.write(outputStream);
        // 写完数据关闭流
        outputStream.close();
    }

    @Override
    public void refushConfBusiness() {
        //全量配置
        EntityWrapper<ConfBusiness> wrapper = new EntityWrapper<>();
        List<ConfBusiness> list = confBusinessService.selectList(wrapper);


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

        //1 检索数据库中已有概念的标的，并将其中的概念抹去
        EntityWrapper<ConfMySotck> cmsWrapper = new EntityWrapper<>();
        cmsWrapper.in("MAIN_BUSINESS", busName).or().in("NICHE_BUSINESS", busName);
        List<ConfMySotck> mySotckList = confMySotckService.selectList(cmsWrapper);

        for (ConfMySotck stock : mySotckList) {
            String mb = stock.getMainBusiness();
            String nb = stock.getNicheBusiness();

            String mbResult = removeBusiness(busName, mb);
            String nbResult = removeBusiness(busName, nb);

            stock.setMainBusiness(mbResult);
            stock.setNicheBusiness(nbResult);
        }
//            confMySotckService.updateBatchById(mySotckList);


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

        EntityWrapper<ConfMySotck> cmsWrapper2 = new EntityWrapper<>();
        cmsWrapper2.in("STOCK_CODE", codeList);
        List<ConfMySotck> mySotckList2 = confMySotckService.selectList(cmsWrapper2);

        //修改库中相关概念
        for (ConfMySotck stock : mySotckList2) {
            String stockCode = stock.getStockCode();
            if (ccList.contains(stockCode)) {
                String business = stock.getMainBusiness();
                String result = addBusiness(busName, business);
                stock.setMainBusiness(result);
            } else {
                String business = stock.getNicheBusiness();
                String result = addBusiness(busName, business);
                stock.setNicheBusiness(result);
            }
        }
//        confMySotckService.insertOrUpdateBatch(mySotckList2, mySotckList2.size());
        log.info("处理成功");
    }

    public String addBusiness(String busName, String business) {
        List<String> list = Lists.newArrayList();
        if (StringUtils.isNotBlank(business)) {
            list = Lists.newArrayList(business.split(","));
        }
        list.add(busName);
        return list.stream().collect(Collectors.joining(","));
    }

    public String removeBusiness(String busName, String business) {
        if (StringUtils.isBlank(business)) {
            return "";
        }
        List<String> list = Lists.newArrayList(business.split(","));
        list.remove(busName);
        return list.stream().collect(Collectors.joining(","));
    }

    @Override
    public void refushAllConfBusiness() {

    }

}
