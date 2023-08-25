package com.example.springBootDemo.dao.mapper;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.example.springBootDemo.entity.input.BaseZbStock;
import com.example.springBootDemo.entity.report.MbReport;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * (BaseZbStock)表数据库访问层
 *
 * @author makejava
 * @since 2023-08-06 00:40:16
 */
@Mapper
public interface BaseZbStockDao extends BaseMapper<BaseZbStock> {

    List<MbReport> getMbReportByDate(String date);
}

