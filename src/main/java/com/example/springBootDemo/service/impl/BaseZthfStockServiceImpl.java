package com.example.springBootDemo.service.impl;


import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.example.springBootDemo.dao.mapper.BaseZthfStockDao;
import com.example.springBootDemo.entity.BaseZthfStock;
import com.example.springBootDemo.entity.report.ZtReport;
import com.example.springBootDemo.service.BaseZthfStockService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * (BaseZthfStock)表服务实现类
 *
 * @author makejava
 * @since 2023-08-06 00:00:11
 */
@Service("baseZthfStockService")
public class BaseZthfStockServiceImpl extends ServiceImpl<BaseZthfStockDao, BaseZthfStock> implements BaseZthfStockService {

    @Resource
    private BaseZthfStockDao baseZthfStockDao;

    @Override
    public List<ZtReport> getZtReportByDate(String date) {
        return baseZthfStockDao.getZtReportByDate(date);
    }
}
