package com.example.springBootDemo.service;

import com.baomidou.mybatisplus.service.IService;
import com.example.springBootDemo.entity.input.BaseZthfStock;
import com.example.springBootDemo.entity.report.ZtReport;

import java.util.List;

;

/**
 * (BaseZthfStock)表服务接口
 *
 * @author makejava
 * @since 2023-08-06 00:00:09
 */
public interface BaseZthfStockService extends IService<BaseZthfStock> {

    List<ZtReport> getZtReportByDate(String date);
}
