package com.example.springBootDemo.service;


import com.baomidou.mybatisplus.service.IService;
import com.example.springBootDemo.entity.input.BaseZtStock;
import com.example.springBootDemo.entity.report.ZtReport;

import java.util.List;

/**
 * (BaseZtStock)表服务接口
 *
 * @author makejava
 * @since 2023-08-05 20:47:19
 */
public interface BaseZtStockService  extends IService<BaseZtStock> {

    List<ZtReport> getZtReportByDate(String date);
}
