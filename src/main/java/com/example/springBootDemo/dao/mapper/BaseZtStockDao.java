package com.example.springBootDemo.dao.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.example.springBootDemo.entity.input.BaseZtStock;
import com.example.springBootDemo.entity.report.ZtReport;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BaseZtStockDao extends BaseMapper<BaseZtStock> {

    List<ZtReport> getZtReportByDate(String date);
}