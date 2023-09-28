package com.example.springBootDemo.service.impl;

import com.alibaba.excel.util.DateUtils;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.example.springBootDemo.config.components.constant.DateTypeConstant;
import com.example.springBootDemo.dao.mapper.BaseDateDao;
import com.example.springBootDemo.entity.BaseDate;
import com.example.springBootDemo.service.BaseDateService;
import com.example.springBootDemo.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;


/**
 * 日期(BaseDate)表服务实现类
 *
 * @author makejava
 * @since 2023-09-03 00:27:46
 */
@Slf4j
@Service("baseDateService")
public class BaseDateServiceImpl extends ServiceImpl<BaseDateDao, BaseDate> implements BaseDateService {
    @Resource
    private BaseDateDao baseDateDao;

    @Override
    public void updateBatchByDate(List<BaseDate> list) {
        baseDateDao.updateBatchByDate(list);
    }

    @Override
    public Date getBeforeTypeDate(Date date, List<String> typeList) {
        return baseDateDao.getBeforeTypeDate(date, typeList);
    }

    @Override
    public String getBeforeTypeDate(String dateStr, List<String> typeList) {
        if (StringUtils.isEmpty(dateStr)) {
            dateStr = DateUtil.format(new Date(), DateUtils.DATE_FORMAT_10);
        }
        Date date = DateUtil.parseDate(dateStr);
        Date resultDate = baseDateDao.getBeforeTypeDate(date, typeList);
        String resultDateStr = DateUtil.format(resultDate, DateUtils.DATE_FORMAT_10);
        log.info("前一个交易日时间为：{}", resultDateStr);
        return resultDateStr;
    }

    @Override
    public Date getAfterTypeDate(Date date, List<String> typeList) {
        return baseDateDao.getAfterTypeDate(date, typeList);
    }

    @Override
    public String getAfterTypeDate(String dateStr, List<String> typeList) {
        if (StringUtils.isEmpty(dateStr)) {
            dateStr = DateUtil.format(new Date(), DateUtils.DATE_FORMAT_10);
        }
        Date date = DateUtil.parseDate(dateStr);
        Date resultDate = baseDateDao.getAfterTypeDate(date, typeList);
        String resultDateStr = DateUtil.format(resultDate, DateUtils.DATE_FORMAT_10);
        log.info("后一个交易日时间为：{}", resultDateStr);
        return resultDateStr;
    }

    @Override
    public String queryDateDetail(String dateStr) {
        if (StringUtils.isEmpty(dateStr)) {
            dateStr = DateUtil.format(new Date(), DateUtils.DATE_FORMAT_10);
        }
        Date date = DateUtil.parseDate(dateStr);
        return queryDateDetail(date);
    }

    /**
     * 查询日期描述明细
     *
     * @param date
     * @return
     */
    @Override
    public String queryDateDetail(Date date) {
        StringBuffer sb = new StringBuffer();
        BaseDate baseDate = queryBaseDateBydate(date);
        String type = baseDate.getType();

        if (DateTypeConstant.DATE_TYPE_HOLIDAY.equals(type)) {
            Date endDate = getAfterTypeDate(date, DateTypeConstant.WORK_LIST);
            Integer countDown = DateUtil.getIntervalOfDays(date, endDate);
            Integer range = DateUtil.getIntervalOfDays(new Date(), date);
            sb.append("距离" + baseDate.getName() + "(" + countDown + ")天假期,还有(" + range + ")天");
        } else if (DateTypeConstant.DATE_TYPE_WORK.equals(type)) {
            Date endDate = getAfterTypeDate(date, DateTypeConstant.WORK_LIST);
            Integer range = DateUtil.getIntervalOfDays(date, endDate);
            sb.append("下一个交易日-" + DateUtil.format(baseDate.getDate(), DateUtils.DATE_FORMAT_10) + "(" + baseDate.getWeek() + "),还有(" + range + ")天");
        } else {
            sb.append("工作日类型还没有配置");
        }
        log.info(sb.toString());
        return sb.toString();
    }

    /**
     * 查询某一天的具体情况
     *
     * @param date
     * @return
     */
    @Override
    public BaseDate queryBaseDateBydate(Date date) {
        return baseDateDao.queryBaseDateBydate(date);
    }
}
