package com.example.springBootDemo.controller;

import com.example.springBootDemo.config.components.system.session.RespBean;
import com.example.springBootDemo.service.*;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
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
    private ConfMySotckService confMySotckService;
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
    @ApiOperation("刷新股票配置信息")
    @PostMapping("/reflshMyStock")
    public RespBean reflshMyStock() {
        try {
            confMySotckService.reflshMyStock();
            return RespBean.success("刷新成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return RespBean.error("刷新失败");
    }

    @ApiOperationSupport(order = 11)
    @ApiOperation("1-1 文件导入次新信息")
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
    @ApiOperation("1-2 文件导入可转债信息")
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
    @ApiOperation("1-3 文件导入股票配置信息")
    @PostMapping("/imporMyStock")
    public RespBean imporMyStock() {
        try {
            confMySotckService.imporMyStock();
            return RespBean.success("导入成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return RespBean.error("导入失败");
    }

    @ApiOperationSupport(order = 21)
    @ApiOperation("2-1 文件查询题材/行业配置信息")
    @GetMapping("/exportConfBusiness")
    public void exportConfBusiness(HttpServletResponse response) {
        try {
            confBusinessService.exportConfBusiness(response);
            log.info("导入成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ApiOperationSupport(order = 22)
    @ApiOperation("2-2 文件导入题材/行业配置信息")
    @PostMapping("/imporConfBusiness")
    public RespBean imporConfBusiness(@RequestPart MultipartFile multipartFile) {
        try {
            boolean flag = confBusinessService.imporConfBusiness(multipartFile);
            if (flag) {
                return RespBean.success("导入成功");
            }
            return RespBean.success("导入成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return RespBean.error("导入失败");
    }
}

