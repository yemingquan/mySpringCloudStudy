package com.example.springBootDemo.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.example.springBootDemo.dao.mapper.ConfMySotckDao;
import com.example.springBootDemo.entity.ConfMySotck;
import com.example.springBootDemo.service.ConfMySotckService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;



/**
 * 配置化自定义个股(ConfMySotck)表服务实现类
 *
 * @author makejava
 * @since 2023-08-28 20:05:16
 */
@Service("confMySotckService")
public class ConfMySotckServiceImpl extends ServiceImpl<ConfMySotckDao, ConfMySotck> implements ConfMySotckService {
    @Resource
    private ConfMySotckDao confMySotckDao;


}
