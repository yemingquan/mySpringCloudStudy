package com.example.springBootDemo.config.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @所属模块<p>
 * @描述<p>
 * @创建人 xiaoYe
 * @创建时间 2025-3-2 0:43
 * @Copyright (c) 2020 inc. all rights reserved<p>
 * @公司名称
 */
@Configurable
public class WebIntercetorConfig implements WebMvcConfigurer {
    @Autowired
    private GlobalInterceptor globalInterceptor;

    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(globalInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/error,/404");
    }
}
