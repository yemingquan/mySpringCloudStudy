package com.example.springBootDemo.dao.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.example.springBootDemo.entity.ConfMySotck;
import org.apache.ibatis.annotations.Mapper;

/**
 * 配置化自定义个股(ConfMySotck)表数据库访问层
 *
 * @author makejava
 * @since 2023-08-28 20:05:15
 */
@Mapper
public interface ConfMySotckDao extends BaseMapper<ConfMySotck>{

}

