package com.example.springBootDemo.dao.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.example.springBootDemo.entity.BaseDateNews;
import com.example.springBootDemo.entity.report.NewsReport;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 新闻(BaseDateNews)表数据库访问层
 *
 * @author makejava
 * @since 2023-09-03 00:27:57
 */
@Mapper
public interface BaseDateNewsDao extends BaseMapper<BaseDateNews>{

    List<NewsReport> getNews(@Param("date") String date);
}

