package com.example.springBootDemo.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.example.springBootDemo.dao.mapper.BaseStockDao;
import com.example.springBootDemo.entity.BaseStock;
import com.example.springBootDemo.service.BaseStockService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;



/**
 * 股票信息(BaseStock)表服务实现类
 *
 * @author makejava
 * @since 2023-10-02 00:28:48
 */
@Service("baseStockService")
public class BaseStockServiceImpl extends ServiceImpl<BaseStockDao, BaseStock> implements BaseStockService {
    @Resource
    private BaseStockDao baseStockDao;

}
