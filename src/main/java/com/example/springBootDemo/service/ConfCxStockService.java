package com.example.springBootDemo.service;

import com.baomidou.mybatisplus.service.IService;
import com.example.springBootDemo.entity.ConfCxStock;


/**
 * 次新股票(ConfCxStock)表服务接口
 *
 * @author makejava
 * @since 2023-08-19 14:39:12
 */
public interface ConfCxStockService extends IService<ConfCxStock>{

    void imporCX() throws Exception;
}
