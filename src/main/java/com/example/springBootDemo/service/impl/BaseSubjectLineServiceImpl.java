package com.example.springBootDemo.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.example.springBootDemo.dao.mapper.BaseSubjectLineDao;
import com.example.springBootDemo.entity.BaseSubjectLine;
import com.example.springBootDemo.service.BaseSubjectLineService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;



/**
 * 题材线(BaseSubjectLine)表服务实现类
 *
 * @author makejava
 * @since 2023-08-26 15:36:17
 */
@Service("baseSubjectLineService")
public class BaseSubjectLineServiceImpl extends ServiceImpl<BaseSubjectLineDao, BaseSubjectLine> implements BaseSubjectLineService {
    @Resource
    private BaseSubjectLineDao baseSubjectLineDao;
}
