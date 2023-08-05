package com.example.springBootDemo.service.impl;


import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.example.springBootDemo.dao.BaseZtStockDao;
import com.example.springBootDemo.domain.BaseZtStock;
import com.example.springBootDemo.service.BaseZtStockService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * (BaseZtStock)表服务实现类
 *
 * @author makejava
 * @since 2023-08-05 20:47:19
 */
@Service("baseZtStockService")
public class BaseZtStockServiceImpl extends ServiceImpl<BaseZtStockDao, BaseZtStock> implements BaseZtStockService {
    @Resource
    private BaseZtStockDao baseZtStockMapper;
}
