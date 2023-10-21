package com.example.springBootDemo.service;

import com.baomidou.mybatisplus.service.IService;
import com.example.springBootDemo.entity.BaseStockMonitor;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;


/**
 * 股票监管池(BaseStockMonitor)表服务接口
 *
 * @author makejava
 * @since 2023-10-21 19:50:08
 */
public interface BaseStockMonitorService extends IService<BaseStockMonitor>{

    void queryBaseStockMonitor(HttpServletResponse response) throws IOException;

    void imporBaseStockMonitor(InputStream inputStream) throws Exception;
}
