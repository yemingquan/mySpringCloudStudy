package com.example.springBootDemo.service;

import org.springframework.web.multipart.MultipartFile;

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
}
