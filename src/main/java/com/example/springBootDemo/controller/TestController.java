package com.example.springBootDemo.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.example.springBootDemo.entity.Student;
import com.example.springBootDemo.service.StudentService;
import com.example.springBootDemo.util.excel.ExcelUtil;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

//MVC层  json格式
@RestController
@RequestMapping("/ssm")
@Api(tags = {"测试模块(功能测试)"})
public class TestController {

    @Autowired
    StudentService studentService;

    @GetMapping("/save")
    public String save(Student po){
        studentService.insert(po);
        return "save success";
    }

    @GetMapping("/del")
    public String del(Long id){
//        studentService.delete(id);
        return "del success";
    }

    @GetMapping("/update")
    public String update(Student stu){
//        Student stu = new Student();
//        stu.setId((long)Math.random());
//        stu.setName("人名"+Math.random());
        studentService.updateById(stu);
        return "update success";
    }

    @GetMapping("/get")
    public Student get(Long id){
        Student stu = studentService.selectById(id);
        return stu;
    }

    @GetMapping("/list")
    public List<Student> list(Student po){
        //根据某年级查询并列举所有数据
        EntityWrapper<Student> wrapper = new EntityWrapper<>();
        wrapper.eq("id",po.getId());
        List<Student> list = studentService.selectList(wrapper);
        return list;
    }

    @ApiOperationSupport(order = 1)
    @GetMapping("/test")
    @ApiOperation("1-0 测试接口")
    public void test(HttpServletResponse response) throws Exception {
//        log.info("1-1{}", 'Ａ' + 1);
//        log.info("1-1{}", ('Ａ' + 1 < 128));
//
//        for (int i = 65313; ; i++) {
//            char c = (char) i;
//            log.info("char:{},字符:{}", i, c);
//        }
    }

    @ApiOperationSupport(order = 9999)
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
}
