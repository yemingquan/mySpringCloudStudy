package com.example.springBootDemo.config.AOP;

import java.lang.annotation.*;

/**
 * @所属模块<p>
 * @描述<p>
 * @创建人 xiaoYe
 * @创建时间 2025-3-3 22:31
 * @Copyright (c) 2020 inc. all rights reserved<p>
 * @公司名称
 */
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogAnnotation {
    String module();

    String name();
}
