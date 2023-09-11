package com.example.springBootDemo.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.example.springBootDemo.config.components.constant.DateTypeConstant;
import com.example.springBootDemo.config.components.enums.NewsEnum;
import com.example.springBootDemo.dao.mapper.BaseDateNewsDao;
import com.example.springBootDemo.entity.BaseDateNews;
import com.example.springBootDemo.entity.report.NewsReport;
import com.example.springBootDemo.service.BaseDateNewsService;
import com.example.springBootDemo.service.BaseDateService;
import com.example.springBootDemo.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;


/**
 * 新闻(BaseDateNews)表服务实现类
 *
 * @author makejava
 * @since 2023-09-03 00:27:57
 */
@Slf4j
@Service("baseDateNewsService")
public class BaseDateNewsServiceImpl extends ServiceImpl<BaseDateNewsDao, BaseDateNews> implements BaseDateNewsService {
    @Resource
    private BaseDateNewsDao baseDateNewsDao;

    @Resource
    private BaseDateNewsService baseDateNewsService;

    @Autowired
    BaseDateService baseDateService;

    @Override
    public List<NewsReport> getNews(String startDate, String date) {
        return baseDateNewsDao.getNews(startDate,date);
    }

    @Override
    public void deleteBaseDateNewsByDateList(String date, String startDate) {
        EntityWrapper<BaseDateNews> detailWr = new EntityWrapper();
        if (StringUtils.isNotBlank(startDate)) {
            detailWr.between("date", startDate, date);
        } else {
            detailWr.eq("date", date);
        }
        delete(detailWr);
    }

    @Override
    public void deleteByCreateDate(String date) {
        EntityWrapper<BaseDateNews> wrapper = new EntityWrapper<>();
        wrapper.eq("create_date", date);
        baseDateNewsService.delete(wrapper);
    }

    @Override
    public void oprNewsData(List<BaseDateNews> list) {
        log.info("处理新闻数据");
        Date dealDate = baseDateService.getBeforeTypeDate(new Date(), DateTypeConstant.DEAL_LIST);

        for (int i = 0; i < list.size(); i++) {
            BaseDateNews news = list.get(i);

            //日期
            Date newsDate = news.getDate();
            if (newsDate == null) {
                newsDate = new Date();
                news.setDate(newsDate);
            }
            Integer duration = news.getDuration();
            if (duration == null) {
                duration = 0;
            }
            //延期或即时
            String type = news.getType();
            if (StringUtils.isBlank(type)) {
                Date lastDay = DateUtil.getNextDay(newsDate, duration);
                if (lastDay.before(dealDate)) {
                    news.setType(NewsEnum.TYPE_INSTANTLY.getName());
                } else {
                    news.setType(NewsEnum.TYPE_FUTURE.getName());
                }
            }
            //脱敏
            String content = news.getContent();
            content = content.replaceAll("领导人","领d人");
            news.setContent(content);
            //影响范围
//                news.setScope(NewsEnum.getCode(NewsConstant.SCOPE, news.getScope()));
            //开盘
//                news.setHappen(NewsEnum.getCode(NewsConstant.HAPPEN, news.getHappen()));
            //创建时间
            news.setCreateDate(new Date());
        }
    }
}
