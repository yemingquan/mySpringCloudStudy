package com.example.springBootDemo.dao.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.example.springBootDemo.entity.ConfModelDetail;
import org.apache.ibatis.annotations.Mapper;

/**
 * 模式打法(ConfModelDetail)表数据库访问层
 *
 * @author makejava
 * @since 2023-10-06 19:07:41
 */
@Mapper
public interface ConfModelDetailDao extends BaseMapper<ConfModelDetail>{

}

