package com.example.springBootDemo.controller;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.example.springBootDemo.config.components.system.session.RespBean;
import com.example.springBootDemo.entity.Student;
import com.example.springBootDemo.service.*;
import com.example.springBootDemo.util.excel.ExcelUtil;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

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

    /***
     * @param multipartFile
     * @return
     */
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


    /***
     * @param multipartFile
     * @return
     */
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

    /***
     * @param multipartFile
     * @return
     */
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


    /***
     * @param multipartFile
     * @return
     */
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


    /***
     * @param multipartFile
     * @return
     */
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


    /***
     * @param multipartFile
     * @return
     */
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


    /***
     * 导入员工数据
     * @param multipartFile
     * @return
     */
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


    /**
     * 导出员工数据excel
     *
     * @param response
     */
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws IOException {
        String title = "excel测试";
        ExportParams params = new ExportParams(title, "sheet1", ExcelType.XSSF);

        EntityWrapper<Student> wrapper = new EntityWrapper<>();
        List<Student> list = studentService.selectList(wrapper);

        ExcelUtil.exportExcel(list, Student.class, title, response, params);
    }


}