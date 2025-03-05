//package com.example.springBootDemo.config.log;
//
//import cn.hutool.core.date.SystemClock;
//import com.github.structlog4j.StructLog4J;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//import javax.annotation.PostConstruct;
//
///**
// * @所属模块<p>
// * @描述<p>
// * @创建人 xiaoYe
// * @创建时间 2025-3-4 23:35
// * @Copyright (c) 2020 inc. all rights reserved<p>
// * @公司名称
// */
//@ConditionalOnClass(WebMvcConfigurer.class)
//public class LogAutoConfig implements WebMvcConfigurer {
//
//    @PostConstruct
//    public void init() {
////        StructLog4J.setFormatter(JsonFormatter.get());
//        StructLog4J.setMandatoryContextSupplier(() -> new Object[]{
//                "host", "host",//InetAddress.getLocalHost().getHostAddress(),
//                "appName", "xxxxx",
//                "logTime", SystemClock.nowDate()
//        });
//    }
//
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(new LogInterceptor()).addPathPatterns("/**");
//        registry.addInterceptor(new HeaderIntrceptor()).addPathPatterns("/**");
//    }
//}
