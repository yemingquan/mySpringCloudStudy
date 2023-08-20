package com.example.springBootDemo.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.example.springBootDemo.config.components.system.session.RespBean;
import com.example.springBootDemo.entity.Student;
import com.example.springBootDemo.entity.report.BdReport;
import com.example.springBootDemo.entity.report.MbReport;
import com.example.springBootDemo.entity.report.ZtReport;
import com.example.springBootDemo.service.ConfBsdStockService;
import com.example.springBootDemo.service.ReportService;
import com.example.springBootDemo.service.StudentService;
import com.example.springBootDemo.util.DateUtil;
import com.example.springBootDemo.util.FileUtil;
import com.example.springBootDemo.util.excel.ExcelChangeUtil;
import com.example.springBootDemo.util.excel.ExcelUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
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
    ReportService reportService;
    @Autowired
    StudentService studentService;
//    @Autowired
//    BaseZtStockService baseZtStockService;
//    @Autowired
//    BaseZthfStockService baseZthfStockService;
//    @Autowired
//    BaseZbStockService baseZbStockService;
//    @Autowired
//    BaseDtStockService baseDtStockService;
//    @Autowired
//    BaseBdDownStockService baseBdDownStockService;
    @Resource
    private ConfBsdStockService confBsdStockService;

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
    @ApiOperation("涨停报表导出")
    public void exportZtRePort(@RequestParam(value = "date", required = false) String date,
                               HttpServletResponse response) {

        String fileName = "涨停1111.xlsx";
        String sheetName = "涨停";
        if (StringUtils.isEmpty(date)) {
            date = DateUtil.format(new Date(), "yyyy-MM-dd");
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
    @ApiOperation("摸板报表导出")
    public void exportMbRePort(@RequestParam(value = "date", required = false) String date,
                               HttpServletResponse response) throws IllegalAccessException, NoSuchFieldException {

        String fileName = "摸板222.xlsx";
        String sheetName = "摸板";

        if (StringUtils.isEmpty(date)) {
            date = DateUtil.format(new Date(), "yyyy-MM-dd");
        }

        List<MbReport> list = reportService.getMbReportByDate(date);

        ExcelUtil<MbReport> excelUtil = new ExcelUtil<>(MbReport.class);
        excelUtil.BSD_STOCK_LIST = confBsdStockService.getBsdList();
        Map<String, Map> annotationMapping = excelUtil.OprMbReport(list);
        excelUtil.exportCustomExcel(annotationMapping, list, fileName, sheetName, response);
    }

    @GetMapping("/exportBdRePort")
    @ApiOperation("波动报表导出")
    public void exportBdRePort(@RequestParam(value = "date", required = false) String date,
                               HttpServletResponse response) throws IllegalAccessException, NoSuchFieldException {

        String fileName = "波动333.xlsx";
        String sheetName = "波动";
        if (StringUtils.isEmpty(date)) {
            date = DateUtil.format(new Date(), "yyyy-MM-dd");
        }
        List<BdReport> list = reportService.getBdReportByDate(date);


        ExcelUtil<BdReport> excelUtil = new ExcelUtil<>(BdReport.class);
        excelUtil.BSD_STOCK_LIST = confBsdStockService.getBsdList();
        Map<String, Map> annotationMapping = excelUtil.OprBdReport(list);
        excelUtil.exportCustomExcel(annotationMapping, list, fileName, sheetName, response);
    }


    @ApiOperation("复盘初始化")
    @PostMapping("/initFP")
    public RespBean initFP(@RequestParam(value = "clearFlag", required = false) String clearFlag,
                           @RequestParam(value = "importDateFlag", required = false) String importDateFlag) {

        String basePath = "C:\\Users\\xiaoYe\\Desktop\\同花顺output\\";
        try {
            //0清理工作-调试阶段不用开启
            if ("1".equals(clearFlag)) {
                File baseDir = new File(basePath);
                Path directory = Paths.get(basePath);
                FileUtil.deleteDirectory(directory);
                baseDir.mkdirs();
                log.info("导出文件已处理");
            }

            //1环境创建-文件夹
            String date = DateUtil.format(new Date(), "yyyy-M-d");
            File baseDir = new File("D:\\DATA\\手机备份数据\\" + date);
            File zsPath = new File(baseDir.getPath() + "\\走势");
            File qtPath = new File(baseDir.getPath() + "\\其他");
            File wpPath = new File(baseDir.getPath() + "\\尾盘");
            FileUtil.mkdirs(baseDir.getPath());
            FileUtil.mkdirs(zsPath.getPath());
            FileUtil.mkdirs(qtPath.getPath());
            FileUtil.mkdirs(wpPath.getPath());
            log.info("图片存储文件夹创立");

            //2将导出的同花顺文件转换成xlsx后，导入到数据库中
            //导入前，需要手动填写主业内容，防止生成的时候统计不对
            if(StringUtils.isEmpty(importDateFlag)){
                return RespBean.success("处理到文件导入前");
            }

            for (int i = 1; i <= 6; i++) {
                File file = new File(basePath + "Table" + i + ".xls");
                File tempFile = ExcelChangeUtil.csvToXlsxConverter(file, file.getName());
                if (i == 1) reportService.importExcelZtStock(new FileInputStream(tempFile));
                if (i == 2) reportService.importExcelZthfStock(new FileInputStream(tempFile));
                if (i == 3) reportService.importExcelZbStock(new FileInputStream(tempFile));
                if (i == 4) reportService.importExcelDtStock(new FileInputStream(tempFile));
                if (i == 5) reportService.importExcelBdUpStock(new FileInputStream(tempFile));
                if (i == 6) reportService.importExcelBdDownStock(new FileInputStream(tempFile));
            }
            return RespBean.success("导入成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return RespBean.error("导入失败");
    }

    @GetMapping("/exportBKReport")
    @ApiOperation("板块报表导出")
    public void exportBKReport(@RequestParam(value = "date", required = false) String date,
                               HttpServletResponse response) {

        String fileName = "板块0000.xlsx";
        String sheetName = "板块";
        if (StringUtils.isEmpty(date)) {
            date = DateUtil.format(new Date(), "yyyy-MM-dd");
        }

        List<ZtReport> list1 = reportService.getZtReportByDate(date);
        List<MbReport> list2 = reportService.getMbReportByDate(date);
        List<BdReport> list3 = reportService.getBdReportByDate(date);
        try {
            reportService.oprZtDate(list1);
            reportService.oprMbDate(list2);
            reportService.oprBdDate(list3);


            reportService.saveZtInstructions(list1);
            reportService.saveMbInstructions(list2);
            reportService.saveBdInstructions(list3);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}