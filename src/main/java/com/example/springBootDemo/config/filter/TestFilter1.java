package com.example.springBootDemo.config.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @所属模块<p>
 * @描述<p> 拦截器学习
 * 他可以针对指定的url进行拦截，并增加一些ip、编码、跨域请求 和重复请求体的逻辑
 *
 * 常见应用场景一句话概括：
 *
 * 安全：认证、防 XSS、防刷
 * 运维：日志、监控、限流
 * 体验：编码统一、跨域支持、GZIP 压缩
 *
 * 定义Filter-1
 * @WebFilter + implements Filter
 *
 * @创建人 xiaoYe
 * @创建时间 2025-11-22 11:46
 * @Copyright (c) 2020 inc. all rights reserved<p>
 * @公司名称
 */
@Slf4j //日志
@WebFilter(filterName = "TestFilter1", urlPatterns = {"/test"}) //拦截器注解，定义名称和拦截路径
@Component //定义为组件，加入到IOC容器
public class TestFilter1 implements Filter {

    /**
     *
     * @param req
     * @param res
     * @param chain
     */
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        //统一编码请求
        req.setCharacterEncoding("UTF-8");
        res.setCharacterEncoding("UTF-8");
//        chain.doFilter(req, res); // 放行

        //权限校验
        HttpServletRequest request = (HttpServletRequest) req;
        String token = request.getHeader("Authorization");

//        if (!isValidToken(token)) {
//            ((HttpServletResponse) res).sendError(401, "Unauthorized");
//            return;
//        }
//        chain.doFilter(req, res);

        //日志记录
        long start = System.currentTimeMillis();
//        chain.doFilter(req, res);
        long duration = System.currentTimeMillis() - start;

        log.info("URI: {}, Method: {}, Time: {}ms",
                request.getRequestURI(),
                request.getMethod(),
                duration);

        //压缩响应
//        HttpServletResponse response = (HttpServletResponse) res;
//        GzipResponseWrapper wrappedResponse = new GzipResponseWrapper(response);
//        chain.doFilter(req, wrappedResponse);
//        wrappedResponse.finish(); // 触发压缩并写回

        //跨域支持
        HttpServletResponse response = (HttpServletResponse) res;
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
        response.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type");

        if ("OPTIONS".equalsIgnoreCase(((HttpServletRequest) req).getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK); // 直接响应预检请求
        } else {
            chain.doFilter(req, res);
        }


        // 基于 IP 或 Token 的简单限流
        String ip = request.getRemoteAddr();
//        if (rateLimiter.isOverLimit(ip)) {
//            response.sendError(429, "Too Many Requests");
//            return;
//        }
//        chain.doFilter(req, res);


//        请求/响应包装（Wrapper）
//        使用 HttpServletRequestWrapper / HttpServletResponseWrapper
//        实现 重复读取请求体（如日志打印 JSON Body）
//        修改响应内容（如统一返回格式）
    }


}
