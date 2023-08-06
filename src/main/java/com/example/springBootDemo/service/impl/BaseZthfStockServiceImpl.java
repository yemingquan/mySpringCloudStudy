package com.example.springBootDemo.service.impl;


import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.example.springBootDemo.dao.BaseZthfStockDao;
import com.example.springBootDemo.entity.BaseZthfStock;
import com.example.springBootDemo.service.BaseZthfStockService;
import org.springframework.stereotype.Service;

/**
 * (BaseZthfStock)表服务实现类
 *
 * @author makejava
 * @since 2023-08-06 00:00:11
 */
@Service("baseZthfStockService")
public class BaseZthfStockServiceImpl extends ServiceImpl<BaseZthfStockDao, BaseZthfStock> implements BaseZthfStockService {

}
