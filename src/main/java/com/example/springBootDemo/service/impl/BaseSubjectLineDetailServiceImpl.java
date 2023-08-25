package com.example.springBootDemo.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.example.springBootDemo.dao.mapper.BaseSubjectLineDetailDao;
import com.example.springBootDemo.entity.BaseSubjectLineDetail;
import com.example.springBootDemo.service.BaseSubjectLineDetailService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


/**
 * 题材线明细(BaseSubjectLineDetail)表服务实现类
 *
 * @author makejava
 * @since 2023-08-25 17:39:59
 */
@Service("baseSubjectLineDetailService")
public class BaseSubjectLineDetailServiceImpl extends ServiceImpl<BaseSubjectLineDetailDao, BaseSubjectLineDetail> implements BaseSubjectLineDetailService {
    @Resource
    private BaseSubjectLineDetailDao baseSubjectLineDetailDao;

}
