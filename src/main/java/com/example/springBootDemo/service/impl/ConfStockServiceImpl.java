package com.example.springBootDemo.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.example.springBootDemo.config.components.constant.DateConstant;
import com.example.springBootDemo.config.components.constant.StockConstant;
import com.example.springBootDemo.config.components.system.SystemConfConstant;
import com.example.springBootDemo.dao.mapper.ConfBsdStockDao;
import com.example.springBootDemo.dao.mapper.ConfStockDao;
import com.example.springBootDemo.entity.ConfBsdStock;
import com.example.springBootDemo.entity.ConfStock;
import com.example.springBootDemo.entity.input.ConfBusiness;
import com.example.springBootDemo.service.ConfBusinessService;
import com.example.springBootDemo.service.ConfDateService;
import com.example.springBootDemo.service.ConfStockService;
import com.example.springBootDemo.util.excel.ExcelChangeUtil;
import com.example.springBootDemo.util.excel.ExcelUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * 配置化自定义个股(ConfStock)表服务实现类
 *
 * @author makejava
 * @since 2023-08-28 20:05:16
 */
@Service("confMyStockService")
public class ConfStockServiceImpl extends ServiceImpl<ConfStockDao, ConfStock> implements ConfStockService {
    @Resource
    private ConfStockDao confStockDao;
    @Resource
    private ConfBsdStockDao confBsdStockDao;
    @Resource
    private ConfDateService confDateService;
    @Resource
    private ConfBusinessService confBusinessService;

    @Override
    public void imporMyStock() throws Exception {
        String basePath = SystemConfConstant.THS_BASE_PATH;
        File file = new File(basePath + "Table_stock.xls");
        File tempFile = ExcelChangeUtil.csvToXlsxConverter(file, file.getName());

        //导入前先删除当天的数据
        EntityWrapper<ConfStock> wrapper = new EntityWrapper<>();
        delete(wrapper);

        List<ConfStock> list = ExcelUtil.excelToList(tempFile, ConfStock.class);
//            is.close();


        List<ConfBsdStock> bsdList = confBsdStockDao.queryStockMonth(null);
        Map<String, String> map = Maps.newHashMap();
        Map<String, List<ConfBsdStock>> stockMap = bsdList.stream().collect(Collectors.groupingBy(ConfBsdStock::getStockCode));
        for (String stockCode : stockMap.keySet()) {
            List<ConfBsdStock> mapList = stockMap.get(stockCode);
            Set set = Sets.newHashSet();

            for (ConfBsdStock po : mapList) {
                String mainBusiness = po.getMainBusiness();
                String nicheBusiness = po.getNicheBusiness();
                if (StringUtils.isNotBlank(mainBusiness))
                    set.addAll(Sets.newHashSet(mainBusiness.replaceAll("\\+", ";").replaceAll("次新", "").replaceAll("最-", "").split(";")));
                if (StringUtils.isNotBlank(nicheBusiness))
                    set.addAll(Sets.newHashSet(nicheBusiness.replaceAll("\\+", ";").replaceAll("次新", "").split(";")));
            }
            set.remove(";");
            set.remove("");

            List<String> tempList = Lists.newArrayList(set);
            String mainBusiness = tempList.stream().collect(Collectors.joining((",")));
            map.put(stockCode, mainBusiness);
        }

        for (ConfStock myStock : list) {
            String mainBusiness = map.get(myStock.getStockCode());
            if (StringUtils.isNotBlank(mainBusiness)) {
                myStock.setMainBusiness(mainBusiness);
            }
        }

        insertBatch(list, list.size());
    }

    @Override
    public void reflshMyStock() {
        //我的股票 全量搜索
        List<ConfStock> list = selectList(new EntityWrapper<>());
        //将6个基础标的股票通过辨识度对象检索出来
//        List<ConfBsdStock> bsdList = confBsdStockDao.queryStockMonth(DateUtil.format(new Date()));
        List<ConfBsdStock> bsdList = confBsdStockDao.queryStockMonth("2023-09-21");
        //股票和主业的映射关系
        Map<String, List<String>> map = Maps.newHashMap();
        Map<String, List<ConfBsdStock>> stockMap = bsdList.stream().collect(Collectors.groupingBy(ConfBsdStock::getStockCode));
        for (String stockCode : stockMap.keySet()) {
            List<ConfBsdStock> mapList = stockMap.get(stockCode);
            Set set = Sets.newHashSet();

            for (ConfBsdStock po : mapList) {
                String mainBusiness = po.getMainBusiness();
                String nicheBusiness = po.getNicheBusiness();
                if (StringUtils.isNotBlank(mainBusiness))
                    set.addAll(Sets.newHashSet(mainBusiness.replaceAll("\\+", ";").replaceAll("最-", "").split(";")));
                if (StringUtils.isNotBlank(nicheBusiness))
                    set.addAll(Sets.newHashSet(nicheBusiness.replaceAll("\\+", ";").replaceAll("最-", "").split(";")));
            }
            set.remove(";");
            set.remove("");

            List<String> tempList = Lists.newArrayList(set);
            map.put(stockCode, tempList);
        }

        //把主业增量修改到 我的股票中
        List<ConfStock> updateList = Lists.newArrayList();
        for (ConfStock myStock : list) {
            List<String> mbList = map.get(myStock.getStockCode());
            if (CollectionUtils.isNotEmpty(mbList)) {
                String oldBusiness = myStock.getMainBusiness() != null ? myStock.getMainBusiness() : "";
                List<String> oldMBList = Lists.newArrayList(oldBusiness.split(","));
                Collection<String> tempList = CollectionUtils.union(oldMBList, mbList);
                tempList.remove(";");
                tempList.remove("");
                String newBusiness = tempList.stream().collect(Collectors.joining((",")));

                myStock.setMainBusiness(newBusiness);
                myStock.setCreateDate(new Date());
                updateList.add(myStock);
            }
        }
        updateBatchById(updateList, updateList.size());
    }

    @Override
    public void reflshCX() throws Exception {
//      查询交易日  次新-新股(883984)5、近端次新(883907)120、远端次新(883974)365


        //查询时间区间内的数据，统计到
        EntityWrapper ew = new EntityWrapper<ConfBusiness>();
        List<String> cxList = StockConstant.CXEnum.getCodeList();
        ew.in("sort", cxList);
        List<ConfBusiness> confBusinessList = confBusinessService.selectList(ew);
        List<ConfBusiness> updateList = Lists.newArrayList();
        Map<String, ConfBusiness> confBusinessMap = confBusinessList.stream().collect(Collectors.toMap(ConfBusiness::getSort, Function.identity(), (item1, item2) -> item2));
        if (confBusinessMap == null) {
            confBusinessMap = Maps.newHashMap();
        }

        for (String sort : cxList) {
            ConfBusiness cb = confBusinessMap.get(sort);
            Integer id = null;
            if (cb != null) {
                id = cb.getId();
            }

            List<ConfStock> list = Lists.newArrayList();
            if (sort.equals(StockConstant.CXEnum.XG.getCode())) {
                Date date5 = confDateService.queryTHSDayLimit(5, DateConstant.DEAL_LIST);
                EntityWrapper confStockEW = new EntityWrapper<ConfStock>();

                confStockEW.ge("issue_date", date5);
                list = confStockDao.selectList(confStockEW);
            } else if (sort.equals(StockConstant.CXEnum.JDCX.getCode())) {
                Date date120 = confDateService.queryTHSDayLimit(120, DateConstant.DEAL_LIST);
                Date date5 = confDateService.queryTHSDayLimit(5, DateConstant.DEAL_LIST);
                EntityWrapper confStockEW = new EntityWrapper<ConfStock>();

                confStockEW.between("issue_date", date120, date5).ne("issue_date",date5);
                list = confStockDao.selectList(confStockEW);
            } else if (sort.equals(StockConstant.CXEnum.YDCX.getCode())) {
                Date date120 = confDateService.queryTHSDayLimit(120, DateConstant.DEAL_LIST);
                Date date365 = confDateService.queryTHSDayLimit(365, DateConstant.DEAL_LIST);
                EntityWrapper confStockEW = new EntityWrapper<ConfStock>();

                confStockEW.between("issue_date", date365, date120).ne("issue_date",date120);;
                list = confStockDao.selectList(confStockEW);
            }


            cb = ConfBusiness.builder()
                    .id(id)
                    .refushFlag("1")
                    .type("属性")
                    .sort(sort)
                    .busName(StockConstant.CXEnum.getName(sort))
                    .relBus("次新")
                    .list(list.stream().map(ConfStock::getStockName).collect(Collectors.joining(",")))
                    .codeList(list.stream().map(ConfStock::getStockCode).collect(Collectors.joining(",")))
                    .createDate(new Date())
                    .createBy("日终刷新")
                    .build();
            updateList.add(cb);
        }
        //插入数据
        confBusinessService.insertOrUpdateBatch(updateList,updateList.size());
        //刷新数据
        for (int i = 0; i < updateList.size(); i++) {
            ConfBusiness cb = updateList.get(i);
            confBusinessService.refushRecordConfBusiness(cb);
        }
    }
}
