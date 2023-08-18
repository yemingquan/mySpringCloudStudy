package com.example.springBootDemo.dao.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.example.springBootDemo.entity.ConfBsdStock;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 辨识度股票(ConfBsdStock)表数据库访问层
 *
 * @author makejava
 * @since 2023-08-17 18:35:09
 */
@Mapper
public interface ConfBsdStockDao extends BaseMapper<ConfBsdStock>{

    List<ConfBsdStock> queryStockMonth(String date);
}

