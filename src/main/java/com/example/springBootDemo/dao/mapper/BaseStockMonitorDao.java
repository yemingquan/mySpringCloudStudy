package com.example.springBootDemo.dao.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.example.springBootDemo.entity.BaseStockMonitor;
import org.apache.ibatis.annotations.Mapper;

/**
 * 股票监管池(BaseStockMonitor)表数据库访问层
 *
 * @author makejava
 * @since 2023-10-21 19:50:08
 */
@Mapper
public interface BaseStockMonitorDao extends BaseMapper<BaseStockMonitor>{

}

