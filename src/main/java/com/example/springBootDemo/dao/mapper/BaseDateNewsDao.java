package com.example.springBootDemo.dao.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.example.springBootDemo.entity.BaseDateNews;
import org.apache.ibatis.annotations.Mapper;

/**
 * 新闻(BaseDateNews)表数据库访问层
 *
 * @author makejava
 * @since 2023-09-03 00:27:57
 */
@Mapper
public interface BaseDateNewsDao extends BaseMapper<BaseDateNews>{

}

