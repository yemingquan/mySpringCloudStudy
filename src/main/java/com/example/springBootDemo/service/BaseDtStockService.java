package com.example.springBootDemo.service;

import com.baomidou.mybatisplus.service.IService;
import com.example.springBootDemo.entity.input.BaseDtStock;
import com.example.springBootDemo.entity.report.MbReport;

import java.util.List;

/**
 * (BaseDtStock)表服务接口
 *
 * @author makejava
 * @since 2023-08-06 01:18:40
 */
public interface BaseDtStockService extends IService<BaseDtStock> {
    List<MbReport> getMbReportByDate(String date);

}
