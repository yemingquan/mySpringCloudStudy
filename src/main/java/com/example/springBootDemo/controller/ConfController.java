package com.example.springBootDemo.controller;

import com.example.springBootDemo.config.components.system.session.RespBean;
import com.example.springBootDemo.service.BaseBondService;
import com.example.springBootDemo.service.ConfBsdStockService;
import com.example.springBootDemo.service.ConfCxStockService;
import com.example.springBootDemo.service.ConfMySotckService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 配置表控制层
 *
 * @author makejava
 * @since 2023-08-17 18:37:58
 */
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


    @ApiOperation("文件导入次新信息")
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

    @ApiOperation("文件导入可转债信息")
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



    @ApiOperation("文件导入股票配置信息")
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


}

