package com.example.springBootDemo.config.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @所属模块<p>
 * @描述<p> 貌似一直不起作用
 * @创建人 xiaoYe
 * @创建时间 2025-3-2 0:43
 * @Copyright (c) 2020 inc. all rights reserved<p>
 * @公司名称
 */
@Configurable
@Slf4j
public class WebIntercetorConfig implements WebMvcConfigurer {


    /**
     * 若要在Interceptor中进行依赖注入，则需要：
     * 将拦截器注册为一个 Bean
     * @return
     */
    @Bean
    public GlobalInterceptor globalInterceptor() {
        return new GlobalInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry){
        log.info("——————————————拦截器配置——————————————");
        registry.addInterceptor(globalInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/error","/404","/static/**", "/resources/**", "/public/**","/doc.html","/swagger-ui/index.html")
        ;
        WebMvcConfigurer.super.addInterceptors(registry);
    }
}
