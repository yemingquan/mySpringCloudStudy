package com.example.springBootDemo.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.example.springBootDemo.entity.BaseDtStock;
import org.apache.ibatis.annotations.Mapper;

/**
 * (BaseDtStock)表数据库访问层
 *
 * @author makejava
 * @since 2023-08-06 01:18:38
 */
@Mapper
public interface BaseDtStockDao extends BaseMapper<BaseDtStock> {

}

