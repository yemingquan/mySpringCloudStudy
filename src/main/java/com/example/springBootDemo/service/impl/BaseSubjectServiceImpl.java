package com.example.springBootDemo.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.example.springBootDemo.dao.mapper.BaseSubjectDao;
import com.example.springBootDemo.entity.BaseSubject;
import com.example.springBootDemo.service.BaseSubjectService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;



/**
 * 题材配置化(BaseSubject)表服务实现类
 *
 * @author makejava
 * @since 2023-08-26 15:36:36
 */
@Service("baseSubjectService")
public class BaseSubjectServiceImpl extends ServiceImpl<BaseSubjectDao, BaseSubject> implements BaseSubjectService {
    @Resource
    private BaseSubjectDao baseSubjectDao;

    @Override
    public void clearSubDate() {
        baseSubjectDao.clearSubDate();
    }
}
