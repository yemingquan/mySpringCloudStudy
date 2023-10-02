package com.example.springBootDemo.dao.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.example.springBootDemo.entity.BaseStock;
import org.apache.ibatis.annotations.Mapper;

/**
 * 股票信息(BaseStock)表数据库访问层
 *
 * @author makejava
 * @since 2023-10-02 00:28:48
 */
@Mapper
public interface BaseStockDao extends BaseMapper<BaseStock>{

}

