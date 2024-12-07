package com.example.springBootDemo.service.impl;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.example.springBootDemo.dao.mapper.BaseZthfStockDao;
import com.example.springBootDemo.entity.input.BaseZthfStock;
import com.example.springBootDemo.entity.report.ZtReport;
import com.example.springBootDemo.service.BaseZthfStockService;
import org.apache.commons.lang3.StringUtils;
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

    @Override
    public void updatePo(BaseZthfStock po) {
        EntityWrapper<BaseZthfStock> wrapper = new EntityWrapper<>();
        wrapper.eq("create_Date", po.getCreateDate());
        if (StringUtils.isNotBlank(po.getStockCode())) {
            wrapper.eq("stock_code", po.getStockCode());
        } else {
            wrapper.eq("stock_name", po.getStockName());
        }
        update(po, wrapper);
    }
}
