package com.example.springBootDemo.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.example.springBootDemo.dao.mapper.ConfModelDetailDao;
import com.example.springBootDemo.entity.ConfModelDetail;
import com.example.springBootDemo.service.ConfModelDetailService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;



/**
 * 模式打法(ConfModelDetail)表服务实现类
 *
 * @author makejava
 * @since 2023-10-06 19:07:41
 */
@Service("confModelDetailService")
public class ConfModelDetailServiceImpl extends ServiceImpl<ConfModelDetailDao, ConfModelDetail> implements ConfModelDetailService {
    @Resource
    private ConfModelDetailDao confModelDetailDao;

}
