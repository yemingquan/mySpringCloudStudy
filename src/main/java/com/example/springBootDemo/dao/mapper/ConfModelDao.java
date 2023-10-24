package com.example.springBootDemo.dao.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.example.springBootDemo.entity.ConfModel;
import com.example.springBootDemo.entity.Model;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 模式，同样一个环境可能会出现多种模式叠加，比如小盘、多概念、消息面、国资委控股(ConfModel)表数据库访问层
 *
 * @author makejava
 * @since 2023-10-06 15:52:23
 */
@Mapper
public interface ConfModelDao extends BaseMapper<ConfModel>{

    List<Model> queryModel();
}

