package com.example.springBootDemo.controller;

import com.example.springBootDemo.service.BaseMarketDetailService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 盘面明细(BaseMarketDetail)表控制层
 *
 * @author makejava
 * @since 2023-09-03 20:07:54
 */
@RestController
@RequestMapping("baseMarketDetail")
public class BaseMarketDetailController {
    /**
     * 服务对象
     */
    @Resource
    private BaseMarketDetailService baseMarketDetailService;

}

