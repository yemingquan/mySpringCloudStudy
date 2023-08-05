package com.example.springBootDemo.controller;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.example.springBootDemo.config.components.system.session.RespBean;
import com.example.springBootDemo.domain.BaseZbStock;
import com.example.springBootDemo.domain.BaseZtStock;
import com.example.springBootDemo.domain.BaseZthfStock;
import com.example.springBootDemo.domain.Student;
import com.example.springBootDemo.service.BaseZbStockService;
import com.example.springBootDemo.service.BaseZtStockService;
import com.example.springBootDemo.service.BaseZthfStockService;
import com.example.springBootDemo.service.StudentService;
import com.example.springBootDemo.util.DateUtil;
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
import java.math.BigDecimal;
import java.util.Date;
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
    StudentService studentService;
    @Autowired
    BaseZtStockService baseZtStockService;
    @Autowired
    BaseZthfStockService baseZthfStockService;
    @Autowired
    BaseZbStockService baseZbStockService;

    /***
     * @param multipartFile
     * @return
     */
    @PostMapping("/importExcelZbStock")
    public RespBean importExcelZbStock(MultipartFile multipartFile) {
        //设置导入参数
        ImportParams importParams = new ImportParams();
        importParams.setHeadRows(1); //表头占1行，默认1

        try {
            //导入前先删除当天的数据
            EntityWrapper<BaseZbStock> wrapper = new EntityWrapper<>();
            wrapper.eq("create_date", DateUtil.format(new Date(),"yyyy-MM-dd"));
            baseZbStockService.delete(wrapper);

            List<BaseZbStock> list = ExcelImportUtil.importExcel(multipartFile.getInputStream(), BaseZbStock.class, importParams);
            list.stream().forEach(po-> {
                po.setCreateDate(new Date());
                po.setModifedDate(new Date());
                BigDecimal before = new BigDecimal(po.getCirculation());
                po.setCirculation(before.divide(new BigDecimal(100000000),2, BigDecimal.ROUND_HALF_UP).doubleValue());
                po.setAmplitude(po.getAmplitude() * 100);
//                po.setYesterdayAmplitude(po.getYesterdayAmplitude() * 100);
                po.setChangingHands(po.getChangingHands() * 100);
//                po.setYesterdayChangingHands(po.getYesterdayChangingHands() * 100);
            });
            if (baseZbStockService.insertBatch(list,list.size())) {
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
        //设置导入参数
        ImportParams importParams = new ImportParams();
        importParams.setHeadRows(1); //表头占1行，默认1

        try {
            //导入前先删除当天的数据
            EntityWrapper<BaseZtStock> wrapper = new EntityWrapper<>();
            wrapper.eq("create_date", DateUtil.format(new Date(),"yyyy-MM-dd"));
            baseZtStockService.delete(wrapper);

            List<BaseZtStock> list = ExcelImportUtil.importExcel(multipartFile.getInputStream(), BaseZtStock.class, importParams);
            list.stream().forEach(po-> {
                po.setCreateDate(new Date());
                po.setModifedDate(new Date());
                BigDecimal before = new BigDecimal(po.getCirculation());
                po.setCirculation(before.divide(new BigDecimal(100000000),2, BigDecimal.ROUND_HALF_UP).doubleValue());
                po.setAmplitude(po.getAmplitude() * 100);
//                po.setYesterdayAmplitude(po.getYesterdayAmplitude() * 100);
                po.setChangingHands(po.getChangingHands() * 100);
//                po.setYesterdayChangingHands(po.getYesterdayChangingHands() * 100);
            });
            if (baseZtStockService.insertBatch(list,list.size())) {
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
        //设置导入参数
        ImportParams importParams = new ImportParams();
        importParams.setHeadRows(1); //表头占1行，默认1

        try {
            //导入前先删除当天的数据
            EntityWrapper<BaseZthfStock> wrapper = new EntityWrapper<>();
            wrapper.eq("create_date", DateUtil.format(new Date(),"yyyy-MM-dd"));
            baseZthfStockService.delete(wrapper);

            List<BaseZthfStock> list = ExcelImportUtil.importExcel(multipartFile.getInputStream(), BaseZthfStock.class, importParams);
            list.stream().forEach(po-> {
                po.setCreateDate(new Date());
                po.setModifedDate(new Date());
                BigDecimal before = new BigDecimal(po.getCirculation());
                po.setCirculation(before.divide(new BigDecimal(100000000),2, BigDecimal.ROUND_HALF_UP).doubleValue());
                po.setAmplitude(po.getAmplitude() * 100);
//                po.setYesterdayAmplitude(po.getYesterdayAmplitude() * 100);
                po.setChangingHands(po.getChangingHands() * 100);
//                po.setYesterdayChangingHands(po.getYesterdayChangingHands() * 100);
            });
            if (baseZthfStockService.insertBatch(list,list.size())) {
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

        ExcelUtil.exportExcel(list, Student.class,title,response,params);
    }


}