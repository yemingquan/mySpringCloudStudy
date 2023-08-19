package com.example.springBootDemo.controller;

import com.example.springBootDemo.service.ConfCxStockService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 次新股票(ConfCxStock)表控制层
 *
 * @author makejava
 * @since 2023-08-19 14:39:12
 */
@RestController
@RequestMapping("confCxStock")
public class ConfCxStockController {
    /**
     * 服务对象
     */
    @Resource
    private ConfCxStockService confCxStockService;
}

