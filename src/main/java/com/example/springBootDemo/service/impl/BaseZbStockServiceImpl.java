package com.example.springBootDemo.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.example.springBootDemo.dao.mapper.BaseZbStockDao;
import com.example.springBootDemo.entity.BaseZbStock;
import com.example.springBootDemo.entity.report.MbReport;
import com.example.springBootDemo.service.BaseZbStockService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * (BaseZbStock)表服务实现类
 *
 * @author makejava
 * @since 2023-08-06 00:40:18
 */
@Service("baseZbStockService")
public class BaseZbStockServiceImpl extends ServiceImpl<BaseZbStockDao, BaseZbStock> implements BaseZbStockService {

    @Resource
    private BaseZbStockDao baseZbStockDao;

    @Override
    public List<MbReport> getMbReportByDate(String date) {
        return baseZbStockDao.getMbReportByDate(date);
    }
}
