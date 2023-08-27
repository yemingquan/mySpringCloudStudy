package com.example.springBootDemo.service;

import com.example.springBootDemo.entity.input.BaseSubjectDetail;
import com.example.springBootDemo.entity.report.BdReport;
import com.example.springBootDemo.entity.report.MbReport;
import com.example.springBootDemo.entity.report.SubjectReport;
import com.example.springBootDemo.entity.report.ZtReport;

import java.text.ParseException;
import java.util.List;

/**
 * @所属模块<p>
 * @描述<p>
 * @创建人 xiaoYe
 * @创建时间 2023-8-6 10:36
 * @Copyright (c) 2020 inc. all rights reserved<p>
 * @公司名称
 */
public interface ReportService {
    void saveMbInstructions(List<MbReport> list);

    void saveZtInstructions(List<ZtReport> list);

    void saveBdInstructions(List<BdReport> list);

    List<ZtReport> getZtReportByDate(String date);

    List<MbReport> getMbReportByDate(String s);

    List<BdReport> getBdReportByDate(String s);

    void oprZtDate(List<ZtReport> list) throws ParseException;

    void oprMbDate(List<MbReport> list);

    void oprBdDate(List<BdReport> list);

    List<SubjectReport> getSubjectReport(String date);

    List<BaseSubjectDetail> genBaseSubjectDetail(List<ZtReport> list1, List<MbReport> list2, List<BdReport> list3);
}
