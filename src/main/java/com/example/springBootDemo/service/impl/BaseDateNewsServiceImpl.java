package com.example.springBootDemo.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.example.springBootDemo.dao.mapper.BaseDateNewsDao;
import com.example.springBootDemo.entity.BaseDateNews;
import com.example.springBootDemo.entity.report.NewsReport;
import com.example.springBootDemo.service.BaseDateNewsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


/**
 * 新闻(BaseDateNews)表服务实现类
 *
 * @author makejava
 * @since 2023-09-03 00:27:57
 */
@Service("baseDateNewsService")
public class BaseDateNewsServiceImpl extends ServiceImpl<BaseDateNewsDao, BaseDateNews> implements BaseDateNewsService {
    @Resource
    private BaseDateNewsDao baseDateNewsDao;

    @Override
    public List<NewsReport> getNews(String date) {
        return baseDateNewsDao.getNews(date);
    }
}
