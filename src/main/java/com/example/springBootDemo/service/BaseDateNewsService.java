package com.example.springBootDemo.service;

import com.baomidou.mybatisplus.service.IService;
import com.example.springBootDemo.entity.BaseDateNews;
import com.example.springBootDemo.entity.report.NewsReport;

import java.util.List;


/**
 * 新闻(BaseDateNews)表服务接口
 *
 * @author makejava
 * @since 2023-09-03 00:27:57
 */
public interface BaseDateNewsService extends IService<BaseDateNews>{

    List<NewsReport> getNews(String date);
}
