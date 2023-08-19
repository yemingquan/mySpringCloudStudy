package com.example.springBootDemo.service;

import com.baomidou.mybatisplus.service.IService;
import com.example.springBootDemo.entity.ConfBsdStock;

import java.util.List;


/**
 * 辨识度股票(ConfBsdStock)表服务接口
 *
 * @author makejava
 * @since 2023-08-17 18:35:09
 */
public interface ConfBsdStockService extends IService<ConfBsdStock>{


    List<ConfBsdStock> genConfBsdStock(String date);

    List<String> getBsdList();
}
