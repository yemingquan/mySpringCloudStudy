package com.example.springBootDemo.dao.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.example.springBootDemo.entity.RelationConf;
import org.apache.ibatis.annotations.Mapper;

/**
 * 模式关系表(RelationConf)表数据库访问层
 *
 * @author makejava
 * @since 2023-10-07 09:11:30
 */
@Mapper
public interface RelationConfDao extends BaseMapper<RelationConf>{

}

