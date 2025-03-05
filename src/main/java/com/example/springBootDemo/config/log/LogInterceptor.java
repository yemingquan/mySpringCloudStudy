package com.example.springBootDemo.config.log;

import cn.hutool.core.lang.UUID;
import org.slf4j.MDC;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @所属模块<p>
 * @描述<p>
 * @创建人 xiaoYe
 * @创建时间 2025-3-5 0:00
 * @Copyright (c) 2020 inc. all rights reserved<p>
 * @公司名称
 */
public class LogInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        MDC.put("traceId", UUID.randomUUID().toString().replaceAll("-","").toLowerCase());
//        String traceId = request.getHeader("app_trace_id");
//        if(StringUtils.isNotEmpty(traceId)){
//            MDC.put("traceId",traceId);
//        }
        return true;
    }
}
