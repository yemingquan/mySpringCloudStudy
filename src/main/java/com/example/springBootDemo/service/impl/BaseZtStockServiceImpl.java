package com.example.springBootDemo.service.impl;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.example.springBootDemo.dao.mapper.BaseZtStockDao;
import com.example.springBootDemo.entity.base.BaseReportStock;
import com.example.springBootDemo.entity.dto.QueryStockDto;
import com.example.springBootDemo.entity.input.BaseZtStock;
import com.example.springBootDemo.entity.report.ZtReport;
import com.example.springBootDemo.service.BaseZtStockService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * (BaseZtStock)表服务实现类
 *
 * @author makejava
 * @since 2023-08-05 20:47:19
 */
@Service("baseZtStockService")
public class BaseZtStockServiceImpl extends ServiceImpl<BaseZtStockDao, BaseZtStock> implements BaseZtStockService {
    @Resource
    private BaseZtStockDao baseZtStockDao;

    @Override
    public List<ZtReport> getZtReportByDate(String date) {
        return baseZtStockDao.getZtReportByDate(date);
    }

    @Override
    public Integer getMaxCombo(QueryStockDto queryStock) {
        return baseZtStockDao.getMaxCombo(queryStock);
    }

    @Override
    public List<BaseZtStock> queryHighStock(Date startDate, Date endDate, int combo) {
            return baseZtStockDao.queryHighStock(startDate,endDate,combo);
    }

    /**
     * 时间和范围暂时都没用，默认sql写死的
     * @param date
     * @return
     */
    @Override
    public List<BaseReportStock> getRecentlyStock(String date) {
        return baseZtStockDao.getRecentlyStock(date);
    }

    @Override
    public void updatePo(BaseZtStock po){
        EntityWrapper<BaseZtStock> wrapper = new EntityWrapper<>();
        wrapper.eq("create_Date", po.getCreateDate());
        if (StringUtils.isNotBlank(po.getStockCode())) {
            wrapper.eq("stock_code", po.getStockCode());
        } else {
            wrapper.eq("stock_name", po.getStockName());
        }
        update(po, wrapper);
    }

}
