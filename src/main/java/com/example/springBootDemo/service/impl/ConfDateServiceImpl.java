package com.example.springBootDemo.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.example.springBootDemo.constant.DateConstant;
import com.example.springBootDemo.dao.mapper.ConfDateDao;
import com.example.springBootDemo.entity.ConfDate;
import com.example.springBootDemo.service.ConfDateService;
import com.example.springBootDemo.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;


/**
 * 日期(ConfDate)表服务实现类
 *
 * @author makejava
 * @since 2023-09-03 00:27:46
 */
@Slf4j
@Service("confDateService")
public class ConfDateServiceImpl extends ServiceImpl<ConfDateDao, ConfDate> implements ConfDateService {
    @Resource
    private ConfDateDao confDateDao;

    @Override
    public void updateBatchByDate(List<ConfDate> list) {
        confDateDao.updateBatchByDate(list);
    }

    @Override
    public Date getBeforeTypeDate(Date date, List<String> typeList) {
        return confDateDao.getBeforeTypeDate(date, typeList);
    }

    @Override
    public String getBeforeTypeDate(String dateStr, List<String> typeList) {
        if (StringUtils.isEmpty(dateStr)) {
            dateStr = DateUtil.format(new Date(), DateConstant.DATE_FORMAT_10);
        }
        Date date = DateUtil.parseDate(dateStr);
        Date resultDate = confDateDao.getBeforeTypeDate(date, typeList);
        String resultDateStr = DateUtil.format(resultDate, DateConstant.DATE_FORMAT_10);
        log.info("前一个交易日时间为：{}", resultDateStr);
        return resultDateStr;
    }

    @Override
    public Date getAfterTypeDate(Date date, List<String> typeList) {
        return confDateDao.getAfterTypeDate(date, typeList);
    }

    @Override
    public String getAfterTypeDate(String dateStr, List<String> typeList) {
        if (StringUtils.isEmpty(dateStr)) {
            dateStr = DateUtil.format(new Date(), DateConstant.DATE_FORMAT_10);
        }
        Date date = DateUtil.parseDate(dateStr);
        Date resultDate = confDateDao.getAfterTypeDate(date, typeList);
        String resultDateStr = DateUtil.format(resultDate, DateConstant.DATE_FORMAT_10);
        log.info("后一个交易日时间为：{}", resultDateStr);
        return resultDateStr;
    }

    @Override
    public String queryDateDetail(String dateStr) {
        if (StringUtils.isEmpty(dateStr)) {
            dateStr = DateUtil.format(new Date(), DateConstant.DATE_FORMAT_10);
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
        ConfDate confDate = queryBaseDateBydate(date);
        String type = confDate.getType();

        if (DateConstant.DATE_TYPE_HOLIDAY.equals(type)) {
            Date endDate = getAfterTypeDate(date, DateConstant.WORK_LIST);
            Integer countDown = DateUtil.getIntervalOfDays(date, endDate);
            Integer range = DateUtil.getIntervalOfDays(new Date(), date);
            sb.append("距离" + confDate.getName() + "(" + countDown + ")天假期,还有(" + range + ")天");
        } else if (DateConstant.DATE_TYPE_WORK.equals(type)) {
            Date endDate = getAfterTypeDate(date, DateConstant.WORK_LIST);
            Integer range = DateUtil.getIntervalOfDays(date, endDate);
            sb.append("下一个交易日-" + DateUtil.format(confDate.getDate(), DateConstant.DATE_FORMAT_10) + "(" + confDate.getWeek() + "),还有(" + range + ")天");
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
    public ConfDate queryBaseDateBydate(Date date) {
        return confDateDao.queryBaseDateBydate(date);
    }

    @Override
    public Integer queryTypeDayLimit(Date startDate, Date endDate, List<String> typeList) {
        return confDateDao.queryTypeDayLimit(startDate, endDate,typeList);
    }

    @Override
    public Date queryTHSDayLimit(int count, List<String> dealList) throws Exception {
        Date baseDate = getBeforeTypeDate(new Date(), dealList);
        ConfDate confDate;
        if (count == 0) {
            return baseDate;
        }
        if (count >= 1) {
            confDate = confDateDao.queryDayLimit(baseDate, count - 1);
        } else {
            confDate = confDateDao.queryDayLimit2(baseDate, Math.abs(count) - 1);
            if (confDate == null) {
                throw new Exception("日期数据暂未更新到指定范围");
            }
        }
        Date date = confDate.getDate();
        if (!DateConstant.DEAL_LIST.contains(confDate.getType())) {
            log.info("距离{}是非工作日，特殊处理", count);
            date = getAfterTypeDate(date, dealList);
        }
        log.info("距离交易日{}-{}天的某个日期是{}", baseDate, count, DateUtil.format(date, DateConstant.DATE_FORMAT_10));
        return date;
    }
}
