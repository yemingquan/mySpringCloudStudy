package com.example.springBootDemo.dao.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.example.springBootDemo.entity.input.BaseDtStock;
import com.example.springBootDemo.entity.report.MbReport;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * (BaseDtStock)表数据库访问层
 *
 * @author makejava
 * @since 2023-08-06 01:18:38
 */
@Mapper
public interface BaseDtStockDao extends BaseMapper<BaseDtStock> {

    List<MbReport> getMbReportByDate(String date);
}

