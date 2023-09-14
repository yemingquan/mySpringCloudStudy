package com.example.springBootDemo.dao.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.example.springBootDemo.entity.input.ConfBusiness;
import org.apache.ibatis.annotations.Mapper;

/**
 * 行业配置化(ConfBusiness)表数据库访问层
 *
 * @author makejava
 * @since 2023-09-13 16:37:28
 */
@Mapper
public interface ConfBusinessDao extends BaseMapper<ConfBusiness>{

}

