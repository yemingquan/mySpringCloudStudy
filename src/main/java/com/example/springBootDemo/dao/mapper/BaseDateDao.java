package com.example.springBootDemo.dao.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.example.springBootDemo.entity.BaseDate;
import org.apache.ibatis.annotations.Mapper;

/**
 * 日期(BaseDate)表数据库访问层
 *
 * @author makejava
 * @since 2023-09-03 00:27:43
 */
@Mapper
public interface BaseDateDao extends BaseMapper<BaseDate>{

}

