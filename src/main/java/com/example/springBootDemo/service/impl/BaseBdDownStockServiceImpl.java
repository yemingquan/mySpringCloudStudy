package com.example.springBootDemo.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.example.springBootDemo.dao.mapper.BaseBdDownStockDao;
import com.example.springBootDemo.entity.BaseBdDownStock;
import com.example.springBootDemo.entity.report.BdReport;
import com.example.springBootDemo.service.BaseBdDownStockService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


/**
 * (BaseBdDownStock)表服务实现类
 *
 * @author makejava
 * @since 2023-08-06 09:48:51
 */
@Service("baseBdDownStockService")
public class BaseBdDownStockServiceImpl extends ServiceImpl<BaseBdDownStockDao, BaseBdDownStock> implements BaseBdDownStockService {
    @Resource
    private BaseBdDownStockDao baseBdDownStockDao;

    @Override
    public List<BdReport> getBdReportByDate(String date) {
        return baseBdDownStockDao.getBdReportByDate(date);
    }
}
