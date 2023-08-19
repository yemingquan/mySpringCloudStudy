package com.example.springBootDemo.service;

import com.example.springBootDemo.entity.report.BdReport;
import com.example.springBootDemo.entity.report.MbReport;
import com.example.springBootDemo.entity.report.ZtReport;

import java.io.InputStream;
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
    void saveZtInstructions(List<ZtReport> list);

    boolean importExcelZthfStock(InputStream is) throws Exception;

    boolean importExcelZtStock(InputStream is) throws Exception;

    boolean importExcelBdUpStock(InputStream is) throws Exception;

    boolean importExcelBdDownStock(InputStream is) throws Exception;

    boolean importExcelDtStock(InputStream is) throws Exception;

    boolean importExcelZbStock(InputStream is) throws Exception;

    List<ZtReport> getZtReportByDate(String date);

    List<MbReport> getMbReportByDate(String s);

    List<BdReport> getBdReportByDate(String s);

    void oprZtDate(List<ZtReport> list) throws ParseException;
}
