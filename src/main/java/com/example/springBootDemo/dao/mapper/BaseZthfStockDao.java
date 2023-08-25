package com.example.springBootDemo.dao.mapper;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.example.springBootDemo.entity.input.BaseZthfStock;
import com.example.springBootDemo.entity.report.ZtReport;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * (BaseZthfStock)表数据库访问层
 *
 * @author makejava
 * @since 2023-08-05 23:59:59
 */
@Mapper
public interface BaseZthfStockDao  extends BaseMapper<BaseZthfStock> {

    List<ZtReport> getZtReportByDate(String date);
}

