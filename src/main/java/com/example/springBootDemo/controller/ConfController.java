package com.example.springBootDemo.controller;

import com.example.springBootDemo.config.components.system.session.RespBean;
import com.example.springBootDemo.service.*;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

/**
 * 配置表控制层
 *
 * @author makejava
 * @since 2023-08-17 18:37:58
 */
@Slf4j
@RestController
@RequestMapping("conf")
@Api(tags = "基础数据-配置")
public class ConfController {

    @Resource
    private ConfBsdStockService confBsdStockService;
    @Resource
    private ConfCxStockService confCxStockService;
    @Resource
    private ConfMyStockService confMyStockService;
    @Resource
    private BaseBondService baseBondService;
    @Resource
    private ConfBusinessService confBusinessService;

    @ApiOperationSupport(order = 1)
    @ApiOperation("根据历史数据生成辨识度股票池")
    @PostMapping("/genConfBsdStock")
    public RespBean genConfBsdStock(@RequestParam(value = "date", required = false) String date) {
        try {
            //检索10天以内的数据
            confBsdStockService.genConfBsdStock(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return RespBean.success("处理成功");
    }

    @ApiOperationSupport(order = 2)
    @ApiOperation("增量刷新股票配置信息")
    @PostMapping("/reflshMyStock")
    public RespBean reflshMyStock() {
        try {
            confMyStockService.reflshMyStock();
            return RespBean.success("刷新成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return RespBean.error("刷新失败");
    }

    @ApiOperationSupport(order = 11)
    @ApiOperation("1-1 文件导入-次新信息")
    @PostMapping("/imporCX")
    public RespBean imporCX() {
        try {
            confCxStockService.imporCX();
            return RespBean.success("导入成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return RespBean.error("导入失败");
    }

    @ApiOperationSupport(order = 12)
    @ApiOperation("1-2 文件导入-可转债信息")
    @PostMapping("/imporKZZ")
    public RespBean imporKZZ() {
        try {
            baseBondService.imporKZZ();
            return RespBean.success("导入成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return RespBean.error("导入失败");
    }

    @ApiOperationSupport(order = 13)
    @ApiOperation("1-3 文件导入-股票配置信息")
    @PostMapping("/imporMyStock")
    public RespBean imporMyStock() {
        try {
            confMyStockService.imporMyStock();
            return RespBean.success("导入成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return RespBean.error("导入失败");
    }

    @ApiOperationSupport(order = 21)
    @ApiOperation("2-1 题材配置-文件查询")
    @GetMapping("/exportConfBusiness")
    public void exportConfBusiness(HttpServletResponse response) {
            try {
            confBusinessService.exportConfBusiness(response, Lists.newArrayList());
            log.info("查询成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ApiOperationSupport(order = 22)
    @ApiOperation("2-2 题材配置-文件查询带同花顺")
    @GetMapping("/exportConfBusinessWithTHS")
    public void exportConfBusinessWithTHS(HttpServletResponse response) {
        try {
            confBusinessService.exportConfBusinessWithTHS(response);
            log.info("查询成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ApiOperationSupport(order = 23)
    @ApiOperation("2-3 题材配置-文件导入")
    @PostMapping("/imporConfBusiness")
    public RespBean imporConfBusiness(@RequestPart MultipartFile multipartFile) {
        try {
            log.info("增量导入的配置会影响myStock表数据，其中excel中的相关概念会被删除，同时会重新按照表格中的数据进行修改");
            confBusinessService.imporConfBusiness(multipartFile);
            return RespBean.success("导入成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return RespBean.error("导入失败");
    }

    @ApiOperationSupport(order = 24)
    @ApiOperation("2-4 题材配置-增量概念刷新")
    @GetMapping("/refushConfBusiness")
    public void refushConfBusiness() {
        try {
            log.info("刷新行业配置表中的新增部分数据，即flash_flag为0的数据。这个数据会被文件导入修改");
            confBusinessService.refushConfBusiness();
            log.info("刷新成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

