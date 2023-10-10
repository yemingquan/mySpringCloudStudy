package com.example.springBootDemo.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.example.springBootDemo.entity.BaseCombo;
import org.apache.ibatis.annotations.Mapper;

/**
 * 连板梯队(BaseCombo)表数据库访问层
 *
 * @author makejava
 * @since 2023-10-08 13:46:58
 */
@Mapper
public interface BaseComboDao extends BaseMapper<BaseCombo>{

}

