package com.example.springBootDemo.dao;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.example.springBootDemo.domain.BaseZthfStock;
import org.apache.ibatis.annotations.Mapper;

/**
 * (BaseZthfStock)表数据库访问层
 *
 * @author makejava
 * @since 2023-08-05 23:59:59
 */
@Mapper
public interface BaseZthfStockDao  extends BaseMapper<BaseZthfStock> {

}

