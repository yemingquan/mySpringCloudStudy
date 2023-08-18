package com.example.springBootDemo.controller;

import com.example.springBootDemo.config.components.system.session.RespBean;
import com.example.springBootDemo.service.ConfBsdStockService;
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
public class ConfController {
    /**
     * 服务对象
     */
    @Resource
    private ConfBsdStockService confBsdStockService;

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

}

