package com.example.springBootDemo.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.example.springBootDemo.dao.mapper.BaseMarketDetailDao;
import com.example.springBootDemo.entity.BaseMarketDetail;
import com.example.springBootDemo.service.BaseMarketDetailService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;



/**
 * 盘面明细(BaseMarketDetail)表服务实现类
 *
 * @author makejava
 * @since 2023-09-03 20:07:54
 */
@Service("baseMarketDetailService")
public class BaseMarketDetailServiceImpl extends ServiceImpl<BaseMarketDetailDao, BaseMarketDetail> implements BaseMarketDetailService {
    @Resource
    private BaseMarketDetailDao baseMarketDetailDao;

}
