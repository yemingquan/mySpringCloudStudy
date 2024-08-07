package com.example.springBootDemo.service;

import com.example.springBootDemo.entity.input.BaseSubjectDetail;

import java.io.InputStream;
import java.util.List;

/**
 * @所属模块<p>
 * @描述<p>
 * @创建人 xiaoYe
 * @创建时间 2023-8-6 10:36
 * @Copyright (c) 2020 inc. all rights reserved<p>
 * @公司名称
 */
public interface InputService {
    boolean importExcelZthfStock(InputStream is) throws Exception;

    boolean importExcelZtStock(InputStream is) throws Exception;

    boolean importExcelBdUpStock(InputStream is) throws Exception;

    boolean importExcelBdDownStock(InputStream is) throws Exception;

    boolean importExcelDtStock(InputStream is) throws Exception;

    boolean importExcelZbStock(InputStream is) throws Exception;

    boolean importSubjectDetail(InputStream inputStream, String startDate, String date) throws Exception;

    void genSubjectDate(List<BaseSubjectDetail> imputList);

    void importStock(String thsBasePath) throws Exception;

    boolean importStock(InputStream inputStream) throws Exception;

    void importExcelBdDownStock(String thsBasePath) throws Exception;

    void importExcelBdUpStock(String thsBasePath) throws Exception;

    void importExcelDtStock(String thsBasePath) throws Exception;

    void importExcelZbStock(String thsBasePath) throws Exception;

    void importExcelZthfStock(String thsBasePath) throws Exception;

    void importExcelZtStock(String thsBasePath) throws Exception;
}
