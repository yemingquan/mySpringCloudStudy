package com.example.springBootDemo.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.example.springBootDemo.dao.mapper.BaseDateDao;
import com.example.springBootDemo.entity.BaseDate;
import com.example.springBootDemo.service.BaseDateService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;


/**
 * 日期(BaseDate)表服务实现类
 *
 * @author makejava
 * @since 2023-09-03 00:27:46
 */
@Service("baseDateService")
public class BaseDateServiceImpl extends ServiceImpl<BaseDateDao, BaseDate> implements BaseDateService {
    @Resource
    private BaseDateDao baseDateDao;

    @Override
    public void updateBatchByDate(List<BaseDate> list) {
        baseDateDao.updateBatchByDate(list);
    }

    @Override
    public Date getBeforeTypeDate(Date createDate, List<String> typeList) {
        return baseDateDao.getBeforeTypeDate(createDate,typeList);
    }
}
