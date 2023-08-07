package com.example.springBootDemo.service.impl.report;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.example.springBootDemo.entity.*;
import com.example.springBootDemo.entity.base.BaseStock;
import com.example.springBootDemo.entity.report.BdReport;
import com.example.springBootDemo.entity.report.MbReport;
import com.example.springBootDemo.entity.report.ZtReport;
import com.example.springBootDemo.service.*;
import com.example.springBootDemo.util.DateUtil;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @所属模块<p>
 * @描述<p>
 * @创建人 xiaoYe
 * @创建时间 2023-8-6 10:36
 * @Copyright (c) 2020 inc. all rights reserved<p>
 * @公司名称
 */
@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    StudentService studentService;
    @Autowired
    BaseZtStockService baseZtStockService;
    @Autowired
    BaseZthfStockService baseZthfStockService;
    @Autowired
    BaseZbStockService baseZbStockService;
    @Autowired
    BaseDtStockService baseDtStockService;
    @Autowired
    BaseBdDownStockService baseBdDownStockService;
    @Autowired
    BaseBdUpStockService baseBdUpStockService;

    @Override
    public List<ZtReport> getZtReportByDate(String date) {
        List<ZtReport> list = Lists.newArrayList();
        List<ZtReport> list1 = baseZtStockService.getZtReportByDate(date);
        List<ZtReport> list2 = baseZthfStockService.getZtReportByDate(date);
        list.addAll(list1);
        list.addAll(list2);
        return list;
    }

    @Override
    public List<MbReport> getMbReportByDate(String date) {
        List<MbReport> list = Lists.newArrayList();
        List<MbReport> list1 = baseZbStockService.getMbReportByDate(date);
        List<MbReport> list2 = baseDtStockService.getMbReportByDate(date);
        list.addAll(list1);
        list.addAll(list2);
        return list;
    }

    @Override
    public List<BdReport> getBdReportByDate(String date) {
        List<BdReport> list = Lists.newArrayList();
        List<BdReport> list1 = baseBdUpStockService.getBdReportByDate(date);
        List<BdReport> list2 = baseBdDownStockService.getBdReportByDate(date);
        list.addAll(list1);
        list.addAll(list2);
        return list;
    }


    @Override
    public boolean importExcelZthfStock(MultipartFile multipartFile) throws Exception {
        //设置导入参数
        ImportParams importParams = new ImportParams();
        importParams.setHeadRows(1); //表头占1行，默认1
        //导入前先删除当天的数据
        EntityWrapper<BaseZthfStock> wrapper = new EntityWrapper<>();
        wrapper.eq("create_date", DateUtil.format(new Date(), "yyyy-MM-dd"));
        baseZthfStockService.delete(wrapper);

        List<BaseZthfStock> list = ExcelImportUtil.importExcel(multipartFile.getInputStream(), BaseZthfStock.class, importParams);
        list.stream().forEach(po -> {
            datePro(po);
        });
        return baseZthfStockService.insertBatch(list, list.size());
    }


    @Override
    public boolean importExcelZtStock(MultipartFile multipartFile) throws Exception {
        //设置导入参数
        ImportParams importParams = new ImportParams();
        importParams.setHeadRows(1); //表头占1行，默认1
        //导入前先删除当天的数据
        EntityWrapper<BaseZtStock> wrapper = new EntityWrapper<>();
        wrapper.eq("create_date", DateUtil.format(new Date(), "yyyy-MM-dd"));
        baseZtStockService.delete(wrapper);

        List<BaseZtStock> list = ExcelImportUtil.importExcel(multipartFile.getInputStream(), BaseZtStock.class, importParams);
        list.stream().forEach(po -> {
            datePro(po);
        });
        return baseZtStockService.insertBatch(list, list.size());
    }

    @Override
    public boolean importExcelBdUpStock(MultipartFile multipartFile) throws Exception {
        //设置导入参数
        ImportParams importParams = new ImportParams();
        importParams.setHeadRows(1); //表头占1行，默认1
        //导入前先删除当天的数据
        EntityWrapper<BaseBdUpStock> wrapper = new EntityWrapper<>();
        wrapper.eq("create_date", DateUtil.format(new Date(), "yyyy-MM-dd"));
        baseBdUpStockService.delete(wrapper);

        List<BaseBdUpStock> list = ExcelImportUtil.importExcel(multipartFile.getInputStream(), BaseBdUpStock.class, importParams);
        list.stream().forEach(po -> {
            datePro(po);
        });
        return baseBdUpStockService.insertBatch(list, list.size());
    }

    @Override
    public boolean importExcelBdDownStock(MultipartFile multipartFile) throws Exception {
        //设置导入参数
        ImportParams importParams = new ImportParams();
        importParams.setHeadRows(1); //表头占1行，默认1
        //导入前先删除当天的数据
        EntityWrapper<BaseBdDownStock> wrapper = new EntityWrapper<>();
        wrapper.eq("create_date", DateUtil.format(new Date(), "yyyy-MM-dd"));
        baseBdDownStockService.delete(wrapper);

        List<BaseBdDownStock> list = ExcelImportUtil.importExcel(multipartFile.getInputStream(), BaseBdDownStock.class, importParams);
        list.stream().forEach(po -> {
            datePro(po);
        });
        return baseBdDownStockService.insertBatch(list, list.size());
    }

    @Override
    public boolean importExcelDtStock(MultipartFile multipartFile) throws Exception {
        //设置导入参数
        ImportParams importParams = new ImportParams();
        importParams.setHeadRows(1); //表头占1行，默认1
        //导入前先删除当天的数据
        EntityWrapper<BaseDtStock> wrapper = new EntityWrapper<>();
        wrapper.eq("create_date", DateUtil.format(new Date(), "yyyy-MM-dd"));
        baseDtStockService.delete(wrapper);

        List<BaseDtStock> list = ExcelImportUtil.importExcel(multipartFile.getInputStream(), BaseDtStock.class, importParams);
        list.stream().forEach(po -> {
            datePro(po);
        });
        return baseDtStockService.insertBatch(list, list.size());
    }

    @Override
    public boolean importExcelZbStock(MultipartFile multipartFile) throws Exception {
        //设置导入参数
        ImportParams importParams = new ImportParams();
        importParams.setHeadRows(1); //表头占1行，默认1
        //导入前先删除当天的数据
        EntityWrapper<BaseZbStock> wrapper = new EntityWrapper<>();
        wrapper.eq("create_date", DateUtil.format(new Date(), "yyyy-MM-dd"));
        baseZbStockService.delete(wrapper);

        List<BaseZbStock> list = ExcelImportUtil.importExcel(multipartFile.getInputStream(), BaseZbStock.class, importParams);
        list.stream().forEach(po -> {
            datePro(po);
        });
        return baseZbStockService.insertBatch(list, list.size());
    }


    private void datePro(BaseStock po) {
        po.setCreateDate(new Date());
        po.setModifedDate(new Date());
        BigDecimal before = new BigDecimal(po.getCirculation());
        po.setCirculation(before.divide(new BigDecimal(100000000), 2, BigDecimal.ROUND_HALF_UP).doubleValue());
        po.setAmplitude(po.getAmplitude() * 100);
//                po.setYesterdayAmplitude(po.getYesterdayAmplitude() * 100);
        po.setChangingHands(po.getChangingHands() * 100);
//                po.setYesterdayChangingHands(po.getYesterdayChangingHands() * 100);
    }
}
