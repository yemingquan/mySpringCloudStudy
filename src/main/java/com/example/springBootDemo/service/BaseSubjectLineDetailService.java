package com.example.springBootDemo.service;

import com.baomidou.mybatisplus.service.IService;
import com.example.springBootDemo.entity.BaseSubjectLineDetail;
import com.example.springBootDemo.entity.report.SubjectReport;

import java.util.Date;
import java.util.List;


/**
 * 题材线明细(BaseSubjectLineDetail)表服务接口
 *
 * @author makejava
 * @since 2023-08-25 17:39:59
 */
public interface BaseSubjectLineDetailService extends IService<BaseSubjectLineDetail>{

    List<SubjectReport> getSubjectReport(String date, String startDate);

    List<BaseSubjectLineDetail> getBaseSubjectLineDetailList(BaseSubjectLineDetail detail);


    void deleteBaseSubjectLineDetailByDateList(String date, String startDate);
}
