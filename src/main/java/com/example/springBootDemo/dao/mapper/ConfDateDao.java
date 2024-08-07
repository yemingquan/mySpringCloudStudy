package com.example.springBootDemo.dao.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.example.springBootDemo.entity.ConfDate;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 日期(ConfDate)表数据库访问层
 *
 * @author makejava
 * @since 2023-09-03 00:27:43
 */
@Mapper
public interface ConfDateDao extends BaseMapper<ConfDate> {

    void updateBatchByDate(@Param("list") List<ConfDate> list);

    Date getBeforeTypeDate(@Param("date") Date date, @Param("typeList") List<String> typeList);

    Date getAfterTypeDate(@Param("date") Date date, @Param("typeList") List<String> typeList);

    ConfDate queryBaseDateBydate(@Param("date") Date date);

    ConfDate queryDayLimit(@Param("baseDate") Date baseDate, @Param("count") int count);

    ConfDate queryDayLimit2(@Param("baseDate") Date baseDate, @Param("count") int count);

    Integer queryTypeDayLimit(@Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("typeList") List<String> typeList);
}

