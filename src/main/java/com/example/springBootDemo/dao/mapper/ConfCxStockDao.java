package com.example.springBootDemo.dao.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.example.springBootDemo.entity.ConfCxStock;
import org.apache.ibatis.annotations.Mapper;

/**
 * 次新股票(ConfCxStock)表数据库访问层
 *
 * @author makejava
 * @since 2023-08-19 14:39:12
 */
@Mapper
public interface ConfCxStockDao extends BaseMapper<ConfCxStock>{

}

