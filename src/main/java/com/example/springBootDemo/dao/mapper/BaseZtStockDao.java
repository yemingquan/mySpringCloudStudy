package com.example.springBootDemo.dao.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.example.springBootDemo.entity.dto.QueryStockDto;
import com.example.springBootDemo.entity.input.BaseZtStock;
import com.example.springBootDemo.entity.report.ZtReport;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BaseZtStockDao extends BaseMapper<BaseZtStock> {

    List<ZtReport> getZtReportByDate(String date);

    Integer getMaxCombo(@Param("dto") QueryStockDto queryStock);
}