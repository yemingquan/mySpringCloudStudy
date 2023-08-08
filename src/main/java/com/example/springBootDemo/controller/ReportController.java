package com.example.springBootDemo.controller;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.example.springBootDemo.config.components.system.session.RespBean;
import com.example.springBootDemo.entity.Student;
import com.example.springBootDemo.entity.report.BdReport;
import com.example.springBootDemo.entity.report.MbReport;
import com.example.springBootDemo.entity.report.ZtReport;
import com.example.springBootDemo.service.ReportService;
import com.example.springBootDemo.service.StudentService;
import com.example.springBootDemo.util.DateUtil;
import com.example.springBootDemo.util.excel.ExcelUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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

    @ApiOperation("导入波动向上Excel数据")
    @PostMapping("/importExcelBdUpStock")
    public RespBean importExcelBdUpStock(MultipartFile multipartFile) {
        try {
            boolean flag = reportService.importExcelBdUpStock(multipartFile);
            if (flag) {
                return RespBean.success("导入成功");
            }
            return RespBean.error("导入失败");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return RespBean.error("导入失败");
    }


    @ApiOperation("导入波动向下Excel数据")
    @PostMapping("/importExcelBdDownStock")
    public RespBean importExcelBdDownStock(MultipartFile multipartFile) {
        try {
            boolean flag = reportService.importExcelBdDownStock(multipartFile);
            if (flag) {
                return RespBean.success("导入成功");
            }
            return RespBean.error("导入失败");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return RespBean.error("导入失败");
    }

    @ApiOperation("导入曾跌停Excel数据")
    @PostMapping("/importExcelDtStock")
    public RespBean importExcelDtStock(MultipartFile multipartFile) {
        try {
            boolean flag = reportService.importExcelDtStock(multipartFile);
            if (flag) {
                return RespBean.success("导入成功");
            }
            return RespBean.error("导入失败");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return RespBean.error("导入失败");
    }


    @ApiOperation("导入炸板Excel数据")
    @PostMapping("/importExcelZbStock")
    public RespBean importExcelZbStock(MultipartFile multipartFile) {
        try {
            boolean flag = reportService.importExcelZbStock(multipartFile);
            if (flag) {
                return RespBean.success("导入成功");
            }
            return RespBean.error("导入失败");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return RespBean.error("导入失败");
    }


    @ApiOperation("导入涨停Excel数据")
    @PostMapping("/importExcelZtStock")
    public RespBean importExcelZtStock(MultipartFile multipartFile) {
        try {
            boolean flag = reportService.importExcelZtStock(multipartFile);
            if (flag) {
                return RespBean.success("导入成功");
            }
            return RespBean.error("导入失败");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return RespBean.error("导入失败");
    }


    @ApiOperation("导入涨停回封Excel数据")
    @PostMapping("/importExcelZthfStock")
    public RespBean importExcelZthfStock(MultipartFile multipartFile) {
        try {
            boolean flag = reportService.importExcelZthfStock(multipartFile);
            if (flag) {
                return RespBean.success("导入成功");
            }
            return RespBean.error("导入失败");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return RespBean.error("导入失败");
    }

    @ApiOperation("Excel导入测试")
    @PostMapping("/import")
    public RespBean importExcel(MultipartFile multipartFile) {
        //设置导入参数
        ImportParams importParams = new ImportParams();
        importParams.setHeadRows(1); //表头占1行，默认1

        try {
            List<Student> list = ExcelImportUtil.importExcel(multipartFile.getInputStream(), Student.class, importParams);
            if (studentService.insertBatch(list)) {
                return RespBean.success("导入成功");
            }
            return RespBean.error("导入失败");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return RespBean.error("导入失败");
    }


    
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
    public void exportZtRePort(HttpServletResponse response) throws IOException, IllegalAccessException, NoSuchFieldException {

        String fileName = "涨停1111.xlsx";
        String sheetName = "涨停";

        String date = DateUtil.format(new Date(), "yyyy-MM-dd");
        List<ZtReport> list = reportService.getZtReportByDate(date);

        ExcelUtil<ZtReport> excelUtil = new ExcelUtil<>(ZtReport.class);
        Map<String, Map> annotationMapping = excelUtil.OprZtReport(list);
        excelUtil.exportCustomExcel(annotationMapping, list, fileName, sheetName, response);
    }

    @GetMapping("/exportMbRePort")
    @ApiOperation("摸板报表导出")
    public void exportMbRePort(HttpServletResponse response) throws IOException, IllegalAccessException, NoSuchFieldException {

        String fileName = "摸板222.xlsx";
        String sheetName = "摸板";

        String date = DateUtil.format(new Date(), "yyyy-MM-dd");
        List<MbReport> list = reportService.getMbReportByDate(date);

        ExcelUtil<MbReport> excelUtil = new ExcelUtil<>(MbReport.class);
        Map<String, Map> annotationMapping = excelUtil.OprMbReport(list);
        excelUtil.exportCustomExcel(annotationMapping, list, fileName, sheetName, response);
    }

    @GetMapping("/exportBdRePort")
    @ApiOperation("波动报表导出")
    public void exportBdRePort(HttpServletResponse response) throws IOException, IllegalAccessException, NoSuchFieldException {

        String fileName = "波动333.xlsx";
        String sheetName = "波动";

        String date = DateUtil.format(new Date(), "yyyy-MM-dd");
        List<BdReport> list = reportService.getBdReportByDate(date);

        ExcelUtil<BdReport> excelUtil = new ExcelUtil<>(BdReport.class);
        Map<String, Map> annotationMapping = excelUtil.OprBdReport(list);
        excelUtil.exportCustomExcel(annotationMapping, list, fileName, sheetName, response);
    }
}