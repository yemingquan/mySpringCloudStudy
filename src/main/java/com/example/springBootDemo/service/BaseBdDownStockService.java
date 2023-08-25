package com.example.springBootDemo.service;

import com.example.springBootDemo.entity.input.BaseBdDownStock;
import com.baomidou.mybatisplus.service.IService;
import com.example.springBootDemo.entity.report.BdReport;

import java.util.List;


/**
 * (BaseBdDownStock)表服务接口
 *
 * @author makejava
 * @since 2023-08-06 09:51:47
 */
public interface BaseBdDownStockService extends IService<BaseBdDownStock>{

    List<BdReport> getBdReportByDate(String date);
}
