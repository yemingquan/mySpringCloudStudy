package com.example.springBootDemo.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.example.springBootDemo.dao.mapper.BaseDtStockDao;
import com.example.springBootDemo.entity.BaseDtStock;
import com.example.springBootDemo.entity.report.MbReport;
import com.example.springBootDemo.service.BaseDtStockService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * (BaseDtStock)表服务实现类
 *
 * @author makejava
 * @since 2023-08-06 01:18:40
 */
@Service("baseDtStockService")
public class BaseDtStockServiceImpl extends ServiceImpl<BaseDtStockDao, BaseDtStock> implements BaseDtStockService {
    @Resource
    private BaseDtStockDao baseDtStockDao;

    @Override
    public List<MbReport> getMbReportByDate(String date) {
        return baseDtStockDao.getMbReportByDate(date);
    }
}