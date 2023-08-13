package com.example.springBootDemo.controller;

import com.example.springBootDemo.service.BaseBondService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 可转债(BaseBond)表控制层
 *
 * @author makejava
 * @since 2023-08-13 23:45:20
 */
@RestController
@RequestMapping("baseBond")
public class BaseBondController {
    /**
     * 服务对象
     */
    @Resource
    private BaseBondService baseBondService;



}

