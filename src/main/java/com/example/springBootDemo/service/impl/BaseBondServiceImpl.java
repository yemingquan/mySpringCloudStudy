package com.example.springBootDemo.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.example.springBootDemo.dao.mapper.BaseBondDao;
import com.example.springBootDemo.entity.input.BaseBond;
import com.example.springBootDemo.service.BaseBondService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;



/**
 * 可转债(BaseBond)表服务实现类
 *
 * @author makejava
 * @since 2023-08-13 23:45:20
 */
@Service("baseBondService")
public class BaseBondServiceImpl extends ServiceImpl<BaseBondDao, BaseBond> implements BaseBondService {
    @Resource
    private BaseBondDao baseBondDao;

}
