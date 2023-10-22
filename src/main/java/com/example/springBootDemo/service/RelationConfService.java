package com.example.springBootDemo.service;

import com.baomidou.mybatisplus.service.IService;
import com.example.springBootDemo.entity.RelationConf;
import com.example.springBootDemo.entity.report.ModelReport;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;


/**
 * 模式关系表(RelationConf)表服务接口
 *
 * @author makejava
 * @since 2023-10-07 09:11:30
 */
public interface RelationConfService extends IService<RelationConf>{

    void queryRelationConf(HttpServletResponse response) throws IOException;

    void imporRelationConf(InputStream inputStream) throws Exception;

    List<ModelReport> exportModelReport(String date);
}
