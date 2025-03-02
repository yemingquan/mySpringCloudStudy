package com.example.springBootDemo.config.interceptor;

import com.example.springBootDemo.config.interceptor.context.BusinessConfig;
import com.example.springBootDemo.config.interceptor.context.ThreadContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

/**
 * @所属模块<p>
 * @描述<p>
 * @创建人 xiaoYe
 * @创建时间 2025-3-2 0:42
 * @Copyright (c) 2020 inc. all rights reserved<p>
 * @公司名称
 */
@Slf4j
@Component
public class GlobalInterceptor implements HandlerInterceptor {
    //TODO 一个简单的json 返回
    public static final String UNACTIVE_MSG = "{\"msg\": \"reject\"}";;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {

        //TODO 需要通过url或者明文的一些字段来确认对应的 appId 、渠道 、产品id 区分不同的环境配置 by对应的字段
        BusinessConfig config = new BusinessConfig();
        // TODO 默认给true
        config.setIsActive(true);
        if (config == null || config.getIsActive() == false) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json;charset=UTF-8");
            response.getOutputStream().write(UNACTIVE_MSG.getBytes(StandardCharsets.UTF_8));
            return false;
        } else {
            ServletInputStream inputStream = request.getInputStream();
            String body = new BufferedReader(new InputStreamReader(inputStream))
                    .lines()
                    .collect(Collectors.joining("\n"));
            config.setOriginRequest(body);
            ThreadContext.put(config);
            return true;
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) {
        ThreadContext.clear();
    }
}
