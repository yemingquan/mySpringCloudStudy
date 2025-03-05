package com.example.springBootDemo.util;

import cn.hutool.core.lang.UUID;
import lombok.extern.slf4j.Slf4j;

/**
 * @所属模块<p>
 * @描述<p>
 * @创建人 xiaoYe
 * @创建时间 2025-3-6 0:03
 * @Copyright (c) 2020 inc. all rights reserved<p>
 * @公司名称
 */
@Slf4j
public class SerialUtil {
    public static String get32UUID() {
        return UUID.randomUUID().toString().replaceAll("-", "").toLowerCase();
    }
}
