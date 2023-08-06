package com.example.springBootDemo.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.example.springBootDemo.entity.BaseZtStock;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BaseZtStockDao extends BaseMapper<BaseZtStock> {

}