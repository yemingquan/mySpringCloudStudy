package com.example.springBootDemo.service.impl;

import com.example.springBootDemo.Dao.StudentDao;
import com.example.springBootDemo.domain.Student;
import com.example.springBootDemo.service.IStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.List;

@Service
@EnableTransactionManagement
public class StudentServiceImpl implements IStudentService {

    @Autowired
    StudentDao dao;

    @Override
    public void save(String name) {
        dao.save(name);
    }

    @Override
    public void delete(Long id) {
        dao.delete(id);
    }

    @Override
    public void update(Student stu) {
        dao.update(stu);
    }

    @Override
    public Student get(Long id) {
        return dao.get(id);
    }

    @Override
    public List<Student> list() {
        return dao.list();
    }
}
