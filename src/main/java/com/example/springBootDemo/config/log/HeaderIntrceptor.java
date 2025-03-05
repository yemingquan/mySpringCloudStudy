package com.example.springBootDemo.config.log;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @所属模块<p>
 * @描述<p>
 * @创建人 xiaoYe
 * @创建时间 2025-3-5 0:04
 * @Copyright (c) 2020 inc. all rights reserved<p>
 * @公司名称
 */
public class HeaderIntrceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
//        String traceId = MDC.get("X-B3-TraceId");
//        MDC.put("traceId", traceId);
//
//        response.setHeader("app_trace_id", traceId);
        return true;
    }

}
