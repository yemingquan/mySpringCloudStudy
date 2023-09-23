package com.example.springBootDemo.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.example.springBootDemo.dao.mapper.BaseMarketDao;
import com.example.springBootDemo.entity.BaseMarket;
import com.example.springBootDemo.service.BaseMarketService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;



/**
 * 市场(BaseMarket)表服务实现类
 *
 * @author makejava
 * @since 2023-09-22 17:51:37
 */
@Service("baseMarketService")
public class BaseMarketServiceImpl extends ServiceImpl<BaseMarketDao, BaseMarket> implements BaseMarketService {
    @Resource
    private BaseMarketDao baseMarketDao;

}
