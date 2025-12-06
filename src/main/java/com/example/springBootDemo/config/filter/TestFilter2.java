package com.example.springBootDemo.config.filter;

import org.springframework.context.annotation.Configuration;

/**
 * @所属模块<p>
 * @描述<p>
 *
 *  Spring Boot 中优先使用 FilterRegistrationBean，便于控制顺序和路径。
 *  定义Filter-2
 *  @Configuration + @Bean
 *
 *  PS：@Configuration 类中的 @Bean 方法支持 依赖注入 和 Bean 间调用优化（见下文 CGLIB 代理）
 *
 * @创建人 xiaoYe
 * @创建时间 2025-11-22 12:42
 * @Copyright (c) 2020 inc. all rights reserved<p>
 * @公司名称
 */
@Configuration
public class TestFilter2 {

//    @Bean
//    public FilterRegistrationBean<AuthFilter> authFilter() {
//        FilterRegistrationBean<AuthFilter> registration = new FilterRegistrationBean<>();
//        registration.setFilter(new AuthFilter());
//        registration.addUrlPatterns("/api/*"); // 指定拦截路径
//        registration.setOrder(1); // 设置执行顺序（值越小越先执行）
//        return registration;
//    }
}
