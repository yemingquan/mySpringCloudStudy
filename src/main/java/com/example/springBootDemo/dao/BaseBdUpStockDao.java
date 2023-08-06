package com.example.springBootDemo.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.example.springBootDemo.entity.BaseBdUpStock;
import org.apache.ibatis.annotations.Mapper;

/**
 * 向上波动的股票(BaseBdUpStock)表数据库访问层
 *
 * @author makejava
 * @since 2023-08-06 10:23:09
 */
@Mapper
public interface BaseBdUpStockDao extends BaseMapper<BaseBdUpStock>{

}

