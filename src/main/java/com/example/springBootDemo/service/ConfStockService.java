package com.example.springBootDemo.service;

import com.baomidou.mybatisplus.service.IService;
import com.example.springBootDemo.entity.ConfStock;
import com.example.springBootDemo.entity.result.ResultstockExcavate;

import java.util.List;


/**
 * 配置化自定义个股(ConfStock)表服务接口
 *
 * @author makejava
 * @since 2023-08-28 20:05:15
 */
public interface ConfStockService extends IService<ConfStock>{


    void imporMyStock() throws Exception;

    void reflshMyStock(String date);

    void reflshCX() throws Exception;

    void reflshSmallStock(String date);

    ConfStock getStockCodeByStockName(String stockName);

    List<ResultstockExcavate> queryStockExcavate(List<String> mainBusinessList, List<String> attrList, String stockName);
}
