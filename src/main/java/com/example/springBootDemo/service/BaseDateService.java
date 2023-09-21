package com.example.springBootDemo.service;

import com.baomidou.mybatisplus.service.IService;
import com.example.springBootDemo.entity.BaseDate;

import java.util.Date;
import java.util.List;


/**
 * 日期(BaseDate)表服务接口
 *
 * @author makejava
 * @since 2023-09-03 00:27:45
 */
public interface BaseDateService extends IService<BaseDate>{

    void updateBatchByDate(List<BaseDate> holidayList);

    Date getBeforeTypeDate(Date createDate, List<String> typeList);

    String getBeforeTypeDate(String date, List<String> typeList);

    Date getAfterTypeDate(Date date, List<String> typeList);

    String getAfterTypeDate(String dateStr, List<String> typeList);

    String queryDateDetail(String dateStr);

    String queryDateDetail(Date dealDate);

    BaseDate queryBaseDateBydate(Date date);
}
