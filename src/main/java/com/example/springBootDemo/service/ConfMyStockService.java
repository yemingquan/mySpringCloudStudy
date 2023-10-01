package com.example.springBootDemo.service;

import com.baomidou.mybatisplus.service.IService;
import com.example.springBootDemo.entity.ConfMyStock;


/**
 * 配置化自定义个股(ConfMyStock)表服务接口
 *
 * @author makejava
 * @since 2023-08-28 20:05:15
 */
public interface ConfMyStockService extends IService<ConfMyStock>{


    void imporMyStock() throws Exception;

    void reflshMyStock();
}
