package com.example.springBootDemo.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.example.springBootDemo.dao.mapper.BaseDateSpecialDao;
import com.example.springBootDemo.entity.BaseDateSpecial;
import com.example.springBootDemo.service.BaseDateSpecialService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;



/**
 * 特殊日期(BaseDateSpecial)表服务实现类
 *
 * @author makejava
 * @since 2023-09-03 00:28:06
 */
@Service("baseDateSpecialService")
public class BaseDateSpecialServiceImpl extends ServiceImpl<BaseDateSpecialDao, BaseDateSpecial> implements BaseDateSpecialService {
    @Resource
    private BaseDateSpecialDao baseDateSpecialDao;

}
