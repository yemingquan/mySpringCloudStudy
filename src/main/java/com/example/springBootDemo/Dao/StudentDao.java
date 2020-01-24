package com.example.springBootDemo.Dao;

import com.example.springBootDemo.domain.Student;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface StudentDao {
    //添加
    @Insert("INSERT INTO student(name) values(#{name})")
    void save(String name);
    //删除
    @Delete("DELETE FROM student where id=#{id}")
    void delete(Long id);
    //修改
    @Update("UPDATE student set name=#{name} where id=#{id}")
    void update(Student stu);
    //查询单个
    @Select("SELECT * FROM student where id=#{id}")
    Student get(Long id);
    //查询多个
    @Select("SELECT * FROM student")
    List<Student> list();
}
