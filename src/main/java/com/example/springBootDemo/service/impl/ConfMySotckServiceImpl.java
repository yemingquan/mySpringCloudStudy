package com.example.springBootDemo.service.impl;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.example.springBootDemo.dao.mapper.ConfBsdStockDao;
import com.example.springBootDemo.dao.mapper.ConfMySotckDao;
import com.example.springBootDemo.entity.ConfBsdStock;
import com.example.springBootDemo.entity.ConfMySotck;
import com.example.springBootDemo.service.ConfMySotckService;
import com.example.springBootDemo.util.excel.ExcelChangeUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * 配置化自定义个股(ConfMySotck)表服务实现类
 *
 * @author makejava
 * @since 2023-08-28 20:05:16
 */
@Service("confMySotckService")
public class ConfMySotckServiceImpl extends ServiceImpl<ConfMySotckDao, ConfMySotck> implements ConfMySotckService {
    @Resource
    private ConfMySotckDao confMySotckDao;
    @Resource
    private ConfBsdStockDao confBsdStockDao;


    @Override
    public void imporMyStock() throws Exception {
        String basePath = "C:\\Users\\xiaoYe\\Desktop\\同花顺output\\";
        File file = new File(basePath + "Table_stock.xls");
        File tempFile = ExcelChangeUtil.csvToXlsxConverter(file, file.getName());

        //设置导入参数
        ImportParams importParams = new ImportParams();
        importParams.setHeadRows(1); //表头占1行，默认1
        //导入前先删除当天的数据
        EntityWrapper<ConfMySotck> wrapper = new EntityWrapper<>();
        delete(wrapper);

        List<ConfMySotck> list = ExcelImportUtil.importExcel(new FileInputStream(tempFile), ConfMySotck.class, importParams);
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

        for (ConfMySotck myStock : list) {
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
        List<ConfMySotck> list = selectList(new EntityWrapper<>());
        //将6个基础标的股票通过辨识度对象检索出来
        List<ConfBsdStock> bsdList = confBsdStockDao.queryStockMonth(null);
        //股票和主业的映射关系
        Map<String, String> map = Maps.newHashMap();
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
            String mainBusiness = tempList.stream().collect(Collectors.joining((",")));
            map.put(stockCode, mainBusiness);
        }

        //把主业全量修改到 我的股票中
        for (ConfMySotck myStock : list) {
            String mainBusiness = map.get(myStock.getStockCode());
            if (StringUtils.isNotBlank(mainBusiness)) {
                myStock.setMainBusiness(mainBusiness);
                myStock.setCreateDate(new Date());
            }
        }

        updateBatchById(list, list.size());
    }
}
