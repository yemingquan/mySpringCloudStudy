package com.example.springBootDemo.dao.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.example.springBootDemo.entity.BaseBdDownStock;
import com.example.springBootDemo.entity.report.BdReport;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * (BaseBdDownStock)表数据库访问层
 *
 * @author makejava
 * @since 2023-08-06 09:52:47
 */
@Mapper
public interface BaseBdDownStockDao extends BaseMapper<BaseBdDownStock>{

    List<BdReport> getBdReportByDate(String date);
}

