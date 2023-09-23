package com.example.springBootDemo.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.example.springBootDemo.dao.mapper.BaseComboDao;
import com.example.springBootDemo.entity.BaseCombo;
import com.example.springBootDemo.service.BaseComboService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;



/**
 * 连板梯队(BaseCombo)表服务实现类
 *
 * @author makejava
 * @since 2023-09-22 17:52:09
 */
@Service("baseComboService")
public class BaseComboServiceImpl extends ServiceImpl<BaseComboDao, BaseCombo> implements BaseComboService {
    @Resource
    private BaseComboDao baseComboDao;

}
