package com.example.springBootDemo.dao.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.example.springBootDemo.entity.input.BaseBdUpStock;
import com.example.springBootDemo.entity.report.BdReport;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 向上波动的股票(BaseBdUpStock)表数据库访问层
 *
 * @author makejava
 * @since 2023-08-06 10:23:09
 */
@Mapper
public interface BaseBdUpStockDao extends BaseMapper<BaseBdUpStock>{

    List<BdReport> getBdReportByDate(String date);
}

