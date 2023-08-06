package com.example.springBootDemo.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.example.springBootDemo.entity.BaseBdDownStock;
import org.apache.ibatis.annotations.Mapper;

/**
 * (BaseBdDownStock)表数据库访问层
 *
 * @author makejava
 * @since 2023-08-06 09:52:47
 */
@Mapper
public interface BaseBdDownStockDao extends BaseMapper<BaseBdDownStock>{

}

