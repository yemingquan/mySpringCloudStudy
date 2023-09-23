package com.example.springBootDemo.dao.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.example.springBootDemo.entity.BaseMarket;
import org.apache.ibatis.annotations.Mapper;

/**
 * 市场(BaseMarket)表数据库访问层
 *
 * @author makejava
 * @since 2023-09-22 17:51:36
 */
@Mapper
public interface BaseMarketDao extends BaseMapper<BaseMarket>{

}

