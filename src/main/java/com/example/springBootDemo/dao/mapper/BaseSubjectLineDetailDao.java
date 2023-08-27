package com.example.springBootDemo.dao.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.example.springBootDemo.entity.BaseSubjectLineDetail;
import com.example.springBootDemo.entity.report.SubjectReport;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 题材线明细(BaseSubjectLineDetail)表数据库访问层
 *
 * @author makejava
 * @since 2023-08-25 17:39:59
 */
@Mapper
public interface BaseSubjectLineDetailDao extends BaseMapper<BaseSubjectLineDetail>{

    List<SubjectReport> getSubjectReport(String date);
}

