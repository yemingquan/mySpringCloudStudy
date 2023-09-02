package com.example.springBootDemo.dao.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.example.springBootDemo.entity.BaseDateSpecial;
import org.apache.ibatis.annotations.Mapper;

/**
 * 特殊日期(BaseDateSpecial)表数据库访问层
 *
 * @author makejava
 * @since 2023-09-03 00:28:06
 */
@Mapper
public interface BaseDateSpecialDao extends BaseMapper<BaseDateSpecial>{

}

