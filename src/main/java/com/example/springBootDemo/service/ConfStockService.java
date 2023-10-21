package com.example.springBootDemo.service;

import com.baomidou.mybatisplus.service.IService;
import com.example.springBootDemo.entity.ConfStock;


/**
 * 配置化自定义个股(ConfStock)表服务接口
 *
 * @author makejava
 * @since 2023-08-28 20:05:15
 */
public interface ConfStockService extends IService<ConfStock>{


    void imporMyStock() throws Exception;

    void reflshMyStock();

    void reflshCX() throws Exception;

    void reflshSmallStock(String date);

    ConfStock getStockCodeByStockName(String stockName);
}
