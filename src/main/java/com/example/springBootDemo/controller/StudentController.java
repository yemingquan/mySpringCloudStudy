package com.example.springBootDemo.controller;

import com.example.springBootDemo.domain.Student;
import com.example.springBootDemo.service.IStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

//MVC层  json格式
@RestController
@RequestMapping("/ssm")
public class StudentController {

    @Autowired
    IStudentService sevice;

    @RequestMapping("/save")
    public String save(String name){
        sevice.save(name);
        return "save success";
    }

    @RequestMapping("/del")
    public String del(Long id){
        sevice.delete(id);
        return "del success";
    }

    @RequestMapping("/update")
    public String update(Student stu){
//        Student stu = new Student();
//        stu.setId((long)Math.random());
//        stu.setName("人名"+Math.random());
        sevice.update(stu);
        return "update success";
    }

    @RequestMapping("/get")
    public Student get(Long id){
        Student stu = sevice.get(id);
        return stu;
    }

    @RequestMapping("/list")
    public List<Student> list(){
        List<Student> list = sevice.list();
        return list;
    }
}
