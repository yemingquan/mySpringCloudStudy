package com.example.springBootDemo.dao.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.example.springBootDemo.entity.BaseSubjectLine;
import org.apache.ibatis.annotations.Mapper;

/**
 * 题材线(BaseSubjectLine)表数据库访问层
 *
 * @author makejava
 * @since 2023-08-26 15:36:17
 */
@Mapper
public interface BaseSubjectLineDao extends BaseMapper<BaseSubjectLine>{

    void clearSubDate();
}

