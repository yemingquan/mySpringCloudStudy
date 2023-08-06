package com.example.springBootDemo.dao;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.example.springBootDemo.entity.BaseZbStock;
import org.apache.ibatis.annotations.Mapper;

/**
 * (BaseZbStock)表数据库访问层
 *
 * @author makejava
 * @since 2023-08-06 00:40:16
 */
@Mapper
public interface BaseZbStockDao extends BaseMapper<BaseZbStock> {


}

