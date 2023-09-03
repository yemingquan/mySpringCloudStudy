package com.example.springBootDemo.dao.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.example.springBootDemo.entity.BaseDate;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 日期(BaseDate)表数据库访问层
 *
 * @author makejava
 * @since 2023-09-03 00:27:43
 */
@Mapper
public interface BaseDateDao extends BaseMapper<BaseDate>{

    void updateBatchByDate(@Param("list") List<BaseDate> list);

    Date getBeforeTypeDate(@Param("date")Date date, @Param("typeList")List<String> typeList);
}

