package com.example.springBootDemo.config.interceptor;

import com.example.springBootDemo.config.log.HeaderIntrceptor;
import com.example.springBootDemo.config.log.LogInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
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
@Configuration
@Slf4j
public class WebIntercetorConfig implements WebMvcConfigurer {
    @Autowired
    private GlobalInterceptor globalInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(globalInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/error","/404","/static/**", "/resources/**", "/public/**","/doc.html","/swagger-ui/index.html")
        ;
        registry.addInterceptor(new LogInterceptor()).addPathPatterns("/**");
        registry.addInterceptor(new HeaderIntrceptor()).addPathPatterns("/**");
        log.info("——————————————拦截器配置完成——————————————");
    }

}
