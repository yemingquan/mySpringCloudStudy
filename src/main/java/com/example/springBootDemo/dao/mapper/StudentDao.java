package com.example.springBootDemo.dao.mapper;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.example.springBootDemo.entity.test.Student;
import org.apache.ibatis.annotations.Mapper;

/**
 * (BStudent)表数据库访问层
 *
 * @author xiaoye
 * @since 2023-08-05 12:46:25
 */
@Mapper
public interface StudentDao extends BaseMapper<Student> {

}