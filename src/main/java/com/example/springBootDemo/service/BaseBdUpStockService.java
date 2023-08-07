package com.example.springBootDemo.service;

import com.example.springBootDemo.entity.BaseBdUpStock;
import com.baomidou.mybatisplus.service.IService;
import com.example.springBootDemo.entity.report.BdReport;

import java.util.List;


/**
 * 向上波动的股票(BaseBdUpStock)表服务接口
 *
 * @author makejava
 * @since 2023-08-06 10:23:09
 */
public interface BaseBdUpStockService extends IService<BaseBdUpStock>{

    List<BdReport> getBdReportByDate(String date);
}
