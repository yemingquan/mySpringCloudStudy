package com.example.springBootDemo.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.example.springBootDemo.config.components.constant.DateTypeConstant;
import com.example.springBootDemo.entity.BaseSubjectLineDetail;
import com.example.springBootDemo.entity.Student;
import com.example.springBootDemo.entity.input.BaseSubjectDetail;
import com.example.springBootDemo.entity.report.*;
import com.example.springBootDemo.service.*;
import com.example.springBootDemo.util.DateUtil;
import com.example.springBootDemo.util.excel.ExcelUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @所属模块<p>
 * @描述<p>
 * @创建人 xiaoYe
 * @创建时间 2023-8-5 8:35
 * @Copyright (c) 2020 inc. all rights reserved<p>
 * @公司名称
 */
@Slf4j
@Api(tags = "基础数据-报表")
@RequestMapping("report")
@RestController
public class ReportController {

    @Autowired
    InputService inputService;
    @Autowired
    ReportService reportService;
    @Autowired
    StudentService studentService;
    @Resource
    private ConfBsdStockService confBsdStockService;
    @Autowired
    BaseDateService baseDateService;
    @Autowired
    BaseSubjectLineDetailService baseSubjectLineDetailService;
    @Autowired
    BaseDateNewsService baseDateNewsService;
    @Resource
    private ConfCxStockService confCxStockService;
    @Resource
    private ConfMySotckService confMySotckService;
    @Resource
    private BaseBondService baseBondService;


    @ApiOperation("Excel导出测试")
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws IOException {
        String fileName = "excel测试.xlsx";
        String sheetName = "excel测试";
//        ExportParams params = new ExportParams(title, "sheet1", ExcelType.XSSF);

        EntityWrapper<Student> wrapper = new EntityWrapper<>();
        List<Student> list = studentService.selectList(wrapper);

        ExcelUtil<Student> excelUtil = new ExcelUtil<>(Student.class);
        excelUtil.exportCustomExcel_bak(list, fileName, sheetName, response);
    }


    @GetMapping("/exportZtRePort")
    @ApiOperation("1-1 涨停报表导出")
    public void exportZtRePort(@RequestParam(value = "date", required = false) String date,
                               @RequestParam(value = "refreshFlag", required = false) String refreshFlag,
                               HttpServletResponse response) {
        log.info("1-1 涨停报表导出 {}", date);
        String fileName = "涨停1111.xlsx";
        String sheetName = "涨停";
        date = baseDateService.getBeforeTypeDate(date, DateTypeConstant.DEAL_LIST);

        if(StringUtils.isNotBlank(refreshFlag)){
            //检索10天以内的数据
            log.info("刷新10天以内的辨识度标的");
            confBsdStockService.genConfBsdStock(date);
        }

        List<ZtReport> list = reportService.getZtReportByDate(date);
        ExcelUtil<ZtReport> excelUtil = new ExcelUtil<>(ZtReport.class);
        excelUtil.BSD_STOCK_LIST = confBsdStockService.getBsdList();
        Map<String, Map> annotationMapping = null;
        try {
            annotationMapping = excelUtil.OprZtReport(list);
            excelUtil.exportCustomExcel(annotationMapping, list, fileName, sheetName, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/exportMbRePort")
    @ApiOperation("1-2 摸板报表导出")
    public void exportMbRePort(@RequestParam(value = "date", required = false) String date,
                               HttpServletResponse response) throws IllegalAccessException, NoSuchFieldException {
        log.info("1-2 摸板报表导出 {}", date);
        String fileName = "摸板222.xlsx";
        String sheetName = "摸板";
        date = baseDateService.getBeforeTypeDate(date, DateTypeConstant.DEAL_LIST);

        List<MbReport> list = reportService.getMbReportByDate(date);

        ExcelUtil<MbReport> excelUtil = new ExcelUtil<>(MbReport.class);
        excelUtil.BSD_STOCK_LIST = confBsdStockService.getBsdList();
        Map<String, Map> annotationMapping = excelUtil.OprMbReport(list);
        excelUtil.exportCustomExcel(annotationMapping, list, fileName, sheetName, response);
    }

    @GetMapping("/exportBdRePort")
    @ApiOperation("1-3 波动报表导出")
    public void exportBdRePort(@RequestParam(value = "date", required = false) String date,
                               HttpServletResponse response) throws IllegalAccessException, NoSuchFieldException {
        log.info("1-3 波动报表导出 {}", date);
        String fileName = "波动333.xlsx";
        String sheetName = "波动";
        date = baseDateService.getBeforeTypeDate(date, DateTypeConstant.DEAL_LIST);
        List<BdReport> list = reportService.getBdReportByDate(date);


        ExcelUtil<BdReport> excelUtil = new ExcelUtil<>(BdReport.class);
        excelUtil.BSD_STOCK_LIST = confBsdStockService.getBsdList();
        Map<String, Map> annotationMapping = excelUtil.OprBdReport(list);
        excelUtil.exportCustomExcel(annotationMapping, list, fileName, sheetName, response);
    }

    @GetMapping("/exportBKReport")
    @ApiOperation("1-0 板块报表导出")
    public void exportBKReport(@RequestParam(value = "date", required = false) String date,
                               @RequestParam(value = "startDate", required = false) String startDate,
                               @RequestParam(value = "clearFlag", required = false) String clearFlag,
                               HttpServletResponse response) {
        log.info("1-0 板块报表导出 date：{} startDate：{} clearFlag:{}", date, startDate, clearFlag);

        long start;
        String fileName = "板块0000.xlsx";
        String sheetName = "板块";
        date = baseDateService.getBeforeTypeDate(date, DateTypeConstant.DEAL_LIST);

        try {
            //获取基础数据，用于后续的数据生成
            List<ZtReport> list1 = reportService.getZtReportByDate(date);
            List<MbReport> list2 = reportService.getMbReportByDate(date);
            List<BdReport> list3 = reportService.getBdReportByDate(date);

            if (CollectionUtils.isEmpty(list3)) {
                log.info("{} 当天没有生成任何数据", date);
                return;
            }

            //导入前先删除当天的数据
            if (StringUtils.isNotBlank(clearFlag)) {
                baseSubjectLineDetailService.deleteBaseSubjectLineDetailByDateList(date, startDate);
            }

            //查询所有明细数据，更新line对象数据
            BaseSubjectLineDetail queryDetail = BaseSubjectLineDetail.builder().createDate(DateUtil.parseDate(date)).build();
            List<BaseSubjectLineDetail> detailList = baseSubjectLineDetailService.getBaseSubjectLineDetailList(queryDetail);

            if (CollectionUtils.isEmpty(detailList) && CollectionUtils.isNotEmpty(list1)) {
                reportService.oprZtDate(list1);
                reportService.oprMbDate(list2);
                reportService.oprBdDate(list3);

                start = System.currentTimeMillis();
                reportService.saveZtInstructions(list1);
                reportService.saveMbInstructions(list2);
                reportService.saveBdInstructions(list3);
                log.info("刷新数据耗时{} ", System.currentTimeMillis() - start);

                //如果当天没有插入,则自动生成默认数据
                List<BaseSubjectDetail> genList = reportService.genBaseSubjectDetail(list1, list2, list3);
                inputService.genSubjectDate(genList);
            }

            //生成市场盘面明细数据
//            reportService.getMarketDetail(list1, list2, list3);

            //上面都是处理当天的逻辑，下面这个是真正的逻辑
            List<SubjectReport> list = reportService.getSubjectReport(date, startDate);
            ExcelUtil<SubjectReport> excelUtil = new ExcelUtil<>(SubjectReport.class);
            start = System.currentTimeMillis();
            Map<String, Map> annotationMapping = excelUtil.OprSubjectReport(list, date);
            log.info("样式配置耗时{} ", System.currentTimeMillis() - start);

            excelUtil.exportCustomExcel(annotationMapping, list, fileName, sheetName, response);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/exportNewsReport")
    @ApiOperation("2-1 消息报表导出")
    public void exportNewsReport(@RequestParam(value = "date", required = false) String date,
                                 HttpServletResponse response) throws IllegalAccessException, NoSuchFieldException {
        log.info("2-1 消息报表导出 {}", date);
        String fileName = "消息.xlsx";
        String sheetName = "消息";
        date = baseDateService.getBeforeTypeDate(date, DateTypeConstant.DEAL_LIST);
        List<NewsReport> list = baseDateNewsService.getNews(date);

        ExcelUtil<NewsReport> excelUtil = new ExcelUtil<>(NewsReport.class);
        Map<String, Map> annotationMapping = excelUtil.OprNewsReport(list);
        excelUtil.exportCustomExcel(annotationMapping, list, fileName, sheetName, response);
    }

    @GetMapping("/exportFinalReport")
    @ApiOperation("2-2 日终报表导出")
    public void exportFinalReport(@RequestParam(value = "date", required = false) String date,
                                  HttpServletResponse response) {
        log.info("2-2 日终报表导出 {}", date);
        String fileName = "日终.xlsx";
        String sheetName = "日终";

        try {
            log.info("次新更新");
            confCxStockService.imporCX();
            log.info("可转债");
            baseBondService.imporKZZ();
            log.info("刷新股票的主业");
            confMySotckService.reflshMyStock();
            log.info("日期功能刷新");

        } catch (Exception e) {
            e.printStackTrace();
        }

//        date = baseDateService.getBeforeTypeDate(date, DateTypeConstant.DEAL_LIST);
//        List<NewsReport> list = baseDateNewsService.getNews(date);
//
//        ExcelUtil<NewsReport> excelUtil = new ExcelUtil<>(NewsReport.class);
//        Map<String, Map> annotationMapping = excelUtil.OprNewsReport(list);
//        excelUtil.exportCustomExcel(annotationMapping, list, fileName, sheetName, response);
    }
}