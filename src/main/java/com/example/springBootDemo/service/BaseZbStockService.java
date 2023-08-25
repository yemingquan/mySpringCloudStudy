package com.example.springBootDemo.service;


import com.baomidou.mybatisplus.service.IService;
import com.example.springBootDemo.entity.input.BaseZbStock;
import com.example.springBootDemo.entity.report.MbReport;

import java.util.List;

/**
 * (BaseZbStock)表服务接口
 *
 * @author makejava
 * @since 2023-08-06 00:40:17
 */
public interface BaseZbStockService extends IService<BaseZbStock> {

    List<MbReport> getMbReportByDate(String date);
}
