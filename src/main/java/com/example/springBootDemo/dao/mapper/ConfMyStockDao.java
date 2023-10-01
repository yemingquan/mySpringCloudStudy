package com.example.springBootDemo.dao.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.example.springBootDemo.entity.ConfMyStock;
import org.apache.ibatis.annotations.Mapper;

/**
 * 配置化自定义个股(ConfMyStock)表数据库访问层
 *
 * @author makejava
 * @since 2023-08-28 20:05:15
 */
@Mapper
public interface ConfMyStockDao extends BaseMapper<ConfMyStock>{

}

