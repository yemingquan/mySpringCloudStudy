package com.example.springBootDemo.dao.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.example.springBootDemo.entity.BaseCombo;
import org.apache.ibatis.annotations.Mapper;

/**
 * 连板梯队(BaseCombo)表数据库访问层
 *
 * @author makejava
 * @since 2023-09-22 17:52:09
 */
@Mapper
public interface BaseComboDao extends BaseMapper<BaseCombo>{

}

