package com.example.springBootDemo.zModuleReport;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.example.springBootDemo.config.components.system.session.RespBean;
import com.example.springBootDemo.domain.StudentPo;
import com.example.springBootDemo.service.StudentService;
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
    StudentService studentService;
    

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
            List<StudentPo> list = ExcelImportUtil.importExcel(multipartFile.getInputStream(),StudentPo.class, importParams);
//            List<StudentPo> list = ExcelUtil.readExcel(file,  StudentPo.class);
//            list.forEach(employee -> {
//            });
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

        EntityWrapper<StudentPo> wrapper = new EntityWrapper<>();
        List<StudentPo> list = studentService.selectList(wrapper);

        ExcelUtil.exportExcel(list,StudentPo.class,title,response,params);

//        // 导出抬头 文件类型
//        ExportParams exportParams = new ExportParams("抬头名称", "sheel名称", ExcelType.XSSF);
//        Workbook workbook = ExcelExportUtil.exportExcel(exportParams, StudentPo.class, list);
//
//        // 拿到comany集合进行导出变为WorkBook 然后写出去就可以了
//        workbook.write(new FileOutputStream(new File("文件地址路径")));



//        Workbook workbook = ExcelExportUtil.exportExcel(params, StudentPo.class, list);
//        ServletOutputStream outputStream = null;
//        try {
//            //流形式
////            response.setHeader("content-type", "application/octet-stream");
//            //中文乱码
//            response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(title+".xlsx", "UTF-8"));
//            outputStream = response.getOutputStream();
//            workbook.write(outputStream);
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (null != outputStream) {
//                try {
//                    outputStream.flush();
//                    outputStream.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
    }


}