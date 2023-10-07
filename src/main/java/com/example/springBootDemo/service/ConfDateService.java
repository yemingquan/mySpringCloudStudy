package com.example.springBootDemo.service;

import com.baomidou.mybatisplus.service.IService;
import com.example.springBootDemo.entity.ConfDate;

import java.util.Date;
import java.util.List;


/**
 * 日期(ConfDate)表服务接口
 *
 * @author makejava
 * @since 2023-09-03 00:27:45
 */
public interface ConfDateService extends IService<ConfDate>{

    void updateBatchByDate(List<ConfDate> holidayList);

    Date getBeforeTypeDate(Date createDate, List<String> typeList);

    String getBeforeTypeDate(String date, List<String> typeList);

    Date getAfterTypeDate(Date date, List<String> typeList);

    String getAfterTypeDate(String dateStr, List<String> typeList);

    String queryDateDetail(String dateStr);

    String queryDateDetail(Date dealDate);

    ConfDate queryBaseDateBydate(Date date);

    /**
     * 查询同花顺规则的交易日距离
     * @param count 距离下个交易日的天数
     * @param dealList 交易类型
     * @return
     * @throws Exception
     */
    Date queryTHSDayLimit(int count, List<String> dealList) throws Exception;
}
