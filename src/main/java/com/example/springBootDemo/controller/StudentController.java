package com.example.springBootDemo.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.example.springBootDemo.entity.Student;
import com.example.springBootDemo.service.StudentService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

//MVC层  json格式
@RestController
@RequestMapping("/ssm")
@Api(tags = {"学生类"})
public class StudentController {

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
}
