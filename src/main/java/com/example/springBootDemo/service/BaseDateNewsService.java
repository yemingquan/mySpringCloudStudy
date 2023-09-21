package com.example.springBootDemo.service;

import com.baomidou.mybatisplus.service.IService;
import com.example.springBootDemo.entity.BaseDateNews;
import com.example.springBootDemo.entity.report.NewsReport;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;


/**
 * 新闻(BaseDateNews)表服务接口
 *
 * @author makejava
 * @since 2023-09-03 00:27:57
 */
public interface BaseDateNewsService extends IService<BaseDateNews>{

    List<NewsReport> getNews(@Param("startDate") String startDate,@Param("date") String date);

    List<NewsReport> getNews(Date startDate, Date date);

    void deleteBaseDateNewsByDateList(String date, String startDate);

    void deleteByCreateDate(String date);

    void oprNewsData(List<BaseDateNews> list);
}
