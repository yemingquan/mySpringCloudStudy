package com.example.springBootDemo.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.example.springBootDemo.dao.mapper.ConfCxStockDao;
import com.example.springBootDemo.entity.ConfCxStock;
import com.example.springBootDemo.service.ConfCxStockService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;



/**
 * 次新股票(ConfCxStock)表服务实现类
 *
 * @author makejava
 * @since 2023-08-19 14:39:12
 */
@Service("confCxStockService")
public class ConfCxStockServiceImpl extends ServiceImpl<ConfCxStockDao, ConfCxStock> implements ConfCxStockService {
    @Resource
    private ConfCxStockDao confCxStockDao;
}
