package com.example.springBootDemo.controller;

import com.example.springBootDemo.config.components.system.session.RespBean;
import com.example.springBootDemo.service.*;
import com.example.springBootDemo.service.impl.task.TaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @所属模块<p>
 * @描述<p>
 * @创建人 xiaoYe
 * @创建时间 2021/2/26 18:19
 * @Copyright (c) 2020 inc. all rights reserved<p>
 * @公司名称
 */
@Slf4j
@RestController
@Api(tags = {"定时任务"})
@RequestMapping("task")
public class TaskController {

    @Autowired
    BaseStockService baseStockService;
    @Autowired
    BaseZtStockService baseZtStockService;
    @Autowired
    BaseZthfStockService baseZthfStockService;
    @Autowired
    BaseZbStockService baseZbStockService;
    @Autowired
    BaseDtStockService baseDtStockService;

    @Autowired
    TaskService taskService;

    @ApiOperation(value = "调试-监控任务")
    @PostMapping(value = "monitorTask", name = "调试-监控任务")
    public RespBean monitorTask(@RequestParam(value = "date", required = false) String date) throws Exception {
        log.info("调试-监控任务");
        taskService.monitorTaskMethod(date);
        return RespBean.success("触发成功");
    }

    @ApiOperation(value = "调试-日终任务")
    @PostMapping(value = "finalTask", name = "调试-日终任务")
    public RespBean finalTask(@RequestParam(value = "date", required = false) String date) throws Exception {
        log.info("调试-监控任务");
        taskService.finalTask(date);
        return RespBean.success("触发成功");
    }
}
