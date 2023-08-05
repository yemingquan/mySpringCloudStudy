//package com.example.springBootDemo.service.impl;
//
//import com.example.springBootDemo.dao.StudentMapper;
//import com.example.springBootDemo.domain.Student;
//import com.example.springBootDemo.service.IStudentService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.EnableTransactionManagement;
//
//import java.util.List;
//
//@Service
//@EnableTransactionManagement
//public class StudentServiceImpl implements IStudentService {
//
//    @Autowired
//    StudentMapper studentDao;
//
//    @Override
//    public void save(String name) {
//        studentDao.save(name);
//    }
//
//    @Override
//    public void delete(Long id) {
//        studentDao.delete(id);
//    }
//
//    @Override
//    public void update(Student stu) {
//        studentDao.update(stu);
//    }
//
//    @Override
//    public Student get(Long id) {
//        return studentDao.get(id);
//    }
//
//    @Override
//    public List<Student> list() {
//        return studentDao.list();
//    }
//}
