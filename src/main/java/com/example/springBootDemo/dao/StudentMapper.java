package com.example.springBootDemo.dao;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.example.springBootDemo.domain.StudentPo;
import org.apache.ibatis.annotations.Mapper;

/**
 * (BStudent)表数据库访问层
 *
 * @author xiaoye
 * @since 2023-08-05 12:46:25
 */
@Mapper
public interface StudentMapper extends BaseMapper<StudentPo> {

}