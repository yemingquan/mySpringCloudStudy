//package com.example.springBootDemo.dao;
//
//import com.example.springBootDemo.domain.Student;
//import org.apache.ibatis.annotations.*;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//
//@Repository
//@Mapper
//public interface StudentMapper {
//    //添加
//    @Insert("INSERT INTO b_student(name) values(#{name})")
//    void save(String name);
//    //删除
//    @Delete("DELETE FROM b_student where id=#{id}")
//    void delete(Long id);
//    //修改
//    @Update("UPDATE b_student set name=#{name} where id=#{id}")
//    void update(Student stu);
//    //查询单个
//    @Select("SELECT * FROM b_student where id=#{id}")
//    Student get(Long id);
//    //查询多个
//    @Select("SELECT * FROM b_student")
//    List<Student> list();
//}
