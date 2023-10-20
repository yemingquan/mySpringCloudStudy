package com.example.springBootDemo.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.example.springBootDemo.config.components.constant.DateConstant;
import com.example.springBootDemo.config.components.enums.NewsEnum;
import com.example.springBootDemo.dao.mapper.BaseDateNewsDao;
import com.example.springBootDemo.entity.BaseDateNews;
import com.example.springBootDemo.entity.input.ConfBusiness;
import com.example.springBootDemo.entity.report.NewsReport;
import com.example.springBootDemo.service.BaseDateNewsService;
import com.example.springBootDemo.service.ConfDateService;
import com.example.springBootDemo.service.ConfBusinessService;
import com.example.springBootDemo.util.DateUtil;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


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
    ConfDateService confDateService;
    @Resource
    private ConfBusinessService confBusinessService;

    @Override
    public List<NewsReport> getNews(String startDate, String date) {
        //检索出所有新闻
        List<NewsReport> list = baseDateNewsDao.getNews(startDate, date);
        //检索出所有板块的核心标的
        List<ConfBusiness> coreList = confBusinessService.queryPlateCore();
        Map<String, String> map = coreList.stream().collect(Collectors.toMap(ConfBusiness::getBusName, ConfBusiness::getCoreList, (item1, item2) -> item1));

        for (NewsReport nr : list) {
            String mainBusiness = nr.getMainBusiness();
            String str = changeMainBusiness(map, mainBusiness);
            nr.setExpect(str);
            int range = DateUtil.getIntervalOfDays(new Date(), nr.getDate());
            nr.setRange(range);
        }
        return list;
    }

    @Override
    public List<NewsReport> getNews(Date startDate, Date date) {
        String startDateStr = DateUtil.format(startDate, DateConstant.DATE_FORMAT_10);
        String dateStr = DateUtil.format(date, DateConstant.DATE_FORMAT_10);
        return getNews(startDateStr, dateStr);
    }

    private String changeMainBusiness(Map<String, String> map, String business) {
        if (StringUtils.isBlank(business) || map == null) {
            return "";
        }
        List<String> list = Lists.newArrayList(business.split(","));
        List<String> resultList = Lists.newArrayList();
        for (String str : list) {
            String value = map.get(str);
            if (value != null) {
                resultList.add(value);
            }
        }
        return resultList.stream().collect(Collectors.joining(","));
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
        Date dealDate = confDateService.getBeforeTypeDate(new Date(), DateConstant.DEAL_LIST);

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
            content = content.replaceAll("领导人", "领d人");
            news.setContent(content);
            //影响范围
//                news.setScope(NewsEnum.getCode(NewsConstant.SCOPE, news.getScope()));
            //开盘
//                news.setHappen(NewsEnum.getCode(NewsConstant.HAPPEN, news.getHappen()));

            //全角逗号转半角
            String mainBusiness = news.getMainBusiness();
            if (StringUtils.isNotBlank(mainBusiness)) {
                mainBusiness = mainBusiness.replaceAll("，", ",");
                news.setMainBusiness(mainBusiness);
            }
            //创建时间
            news.setCreateDate(new Date());
        }
    }
}
