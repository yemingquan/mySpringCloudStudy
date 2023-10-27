package com.example.springBootDemo.dao.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.example.springBootDemo.entity.ConfStock;
import com.example.springBootDemo.entity.result.ResultstockExcavate;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 配置化自定义个股(ConfStock)表数据库访问层
 *
 * @author makejava
 * @since 2023-08-28 20:05:15
 */
@Mapper
public interface ConfStockDao extends BaseMapper<ConfStock> {

    List<ResultstockExcavate> queryStockExcavate(@Param("mainBusinessList") List<String> mainBusinessList, @Param("attrList") List<String> attrList, @Param("stockName") String stockName);
}

