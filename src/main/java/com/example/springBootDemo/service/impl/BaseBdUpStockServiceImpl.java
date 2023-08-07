package com.example.springBootDemo.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.example.springBootDemo.dao.mapper.BaseBdUpStockDao;
import com.example.springBootDemo.entity.BaseBdUpStock;
import com.example.springBootDemo.entity.report.BdReport;
import com.example.springBootDemo.service.BaseBdUpStockService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


/**
 * 向上波动的股票(BaseBdUpStock)表服务实现类
 *
 * @author makejava
 * @since 2023-08-06 10:23:09
 */
@Service("baseBdUpStockService")
public class BaseBdUpStockServiceImpl extends ServiceImpl<BaseBdUpStockDao, BaseBdUpStock> implements BaseBdUpStockService {
    @Resource
    private BaseBdUpStockDao baseBdUpStockDao;

    @Override
    public List<BdReport> getBdReportByDate(String date) {
        return baseBdUpStockDao.getBdReportByDate(date);
    }
}
