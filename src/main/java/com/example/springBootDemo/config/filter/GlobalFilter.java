package com.example.springBootDemo.config.filter;

import com.example.springBootDemo.config.filter.wrapper.RequestWrapper;
import com.example.springBootDemo.config.filter.wrapper.ResponseWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @所属模块<p>
 * @描述<p>
 * @创建人 xiaoYe
 * @创建时间 2025-3-2 0:57
 * @Copyright (c) 2020 inc. all rights reserved<p>
 * @公司名称
 */
@Slf4j
@WebFilter(filterName = "GlobalFilter", urlPatterns = {"/*"})
@Component
public class GlobalFilter implements Filter {


    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {

        ServletRequestWrapper requestWrapper = null;
        if (req instanceof HttpServletRequest) {
            HttpServletRequest httpServletRequest = (HttpServletRequest) req;
            if (httpServletRequest.getRequestURI().equals("/check")) {
                chain.doFilter(req, res);
                return;
            } else {
                requestWrapper = new RequestWrapper(httpServletRequest);
            }

        }

        ResponseWrapper responseWrapper = null;
        if (res instanceof HttpServletResponse) {
            responseWrapper = new ResponseWrapper((HttpServletResponse) res);
        }

        chain.doFilter(requestWrapper == null ? req : requestWrapper, responseWrapper == null ? res : responseWrapper);

        if (responseWrapper != null) {
            res.setContentType(responseWrapper.getContentType());
            res.setContentLength(responseWrapper.getResponseBody().length);
            res.getOutputStream().write(responseWrapper.getResponseBody());
        }
    }


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
//        Filter.super.init(filterConfig);
    }
}
