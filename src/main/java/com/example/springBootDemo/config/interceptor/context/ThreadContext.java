package com.example.springBootDemo.config.interceptor.context;

/**
 * @所属模块<p>
 * @描述<p>
 * @创建人 xiaoYe
 * @创建时间 2025-3-2 10:44
 * @Copyright (c) 2020 inc. all rights reserved<p>
 * @公司名称
 */
public class ThreadContext {
    private static final ThreadLocal<BusinessConfig> contextHolder = new ThreadLocal<>();

    public static void put(BusinessConfig businessConfig) {
        contextHolder.set(businessConfig);
    }

    public static BusinessConfig get() {
        return contextHolder.get();
    }

    public static void clear() {
        contextHolder.remove();
    }
}
