package com.example.springBootDemo.dao.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.example.springBootDemo.entity.BaseMarketDetail;
import org.apache.ibatis.annotations.Mapper;

/**
 * 盘面明细(BaseMarketDetail)表数据库访问层
 *
 * @author makejava
 * @since 2023-09-03 20:07:54
 */
@Mapper
public interface BaseMarketDetailDao extends BaseMapper<BaseMarketDetail>{

}

