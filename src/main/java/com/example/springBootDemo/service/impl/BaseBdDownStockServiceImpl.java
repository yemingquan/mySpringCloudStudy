package com.example.springBootDemo.service.impl;

import com.example.springBootDemo.entity.BaseBdDownStock;
import com.example.springBootDemo.dao.mapper.BaseBdDownStockDao;
import com.example.springBootDemo.service.BaseBdDownStockService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;



/**
 * (BaseBdDownStock)表服务实现类
 *
 * @author makejava
 * @since 2023-08-06 09:48:51
 */
@Service("baseBdDownStockService")
public class BaseBdDownStockServiceImpl extends ServiceImpl<BaseBdDownStockDao, BaseBdDownStock> implements BaseBdDownStockService {


}
