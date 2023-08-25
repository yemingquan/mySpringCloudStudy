package com.example.springBootDemo.dao.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.example.springBootDemo.entity.input.BaseBond;
import org.apache.ibatis.annotations.Mapper;

/**
 * 可转债(BaseBond)表数据库访问层
 *
 * @author makejava
 * @since 2023-08-13 23:45:20
 */
@Mapper
public interface BaseBondDao extends BaseMapper<BaseBond>{

}

