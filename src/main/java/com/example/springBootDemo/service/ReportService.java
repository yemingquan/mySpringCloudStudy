package com.example.springBootDemo.service;

import com.example.springBootDemo.entity.report.BdReport;
import com.example.springBootDemo.entity.report.MbReport;
import com.example.springBootDemo.entity.report.ZtReport;
import org.springframework.web.multipart.MultipartFile;

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
    boolean importExcelZthfStock(MultipartFile multipartFile) throws Exception;

    boolean importExcelZtStock(MultipartFile multipartFile) throws Exception;

    boolean importExcelBdUpStock(MultipartFile multipartFile) throws Exception;

    boolean importExcelBdDownStock(MultipartFile multipartFile) throws Exception;

    boolean importExcelDtStock(MultipartFile multipartFile) throws Exception;

    boolean importExcelZbStock(MultipartFile multipartFile) throws Exception;

    List<ZtReport> getZtReportByDate(String date);

    List<MbReport> getMbReportByDate(String s);

    List<BdReport> getBdReportByDate(String s);
}
