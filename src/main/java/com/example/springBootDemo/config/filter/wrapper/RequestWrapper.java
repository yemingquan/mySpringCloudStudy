package com.example.springBootDemo.config.filter.wrapper;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ArrayUtil;
import lombok.Setter;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;

/**
 * @所属模块<p>
 * @描述<p>
 * @创建人 xiaoYe
 * @创建时间 2025-3-2 1:19
 * @Copyright (c) 2020 inc. all rights reserved<p>
 * @公司名称
 */
@Setter
public class RequestWrapper extends HttpServletRequestWrapper {
    private byte[] requestBody;
    private Map<String, String[]> parameterMap;
    private HttpServletRequest request;

    @Override
    public Map<String, String[]> getParameterMap() {
        if (this.parameterMap == null) {
            return request.getParameterMap();
        }
        return this.parameterMap;
    }

    public void setRequestBody(byte[] requestBody) {
        this.requestBody = requestBody;
    }

    public RequestWrapper(HttpServletRequest request) {
        super(request);
        this.request = request;
        this.parameterMap = request.getParameterMap();
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(this.getInputStream()));
    }

    @Override
    public String getParameter(String name) {
        String[] values = this.getParameterMap().get(name);
        if (ArrayUtil.isEmpty(values)) {
            return null;
        }
        return values[0];
    }

    @Override
    public Enumeration<String> getParameterNames() {
        return Collections.enumeration(this.getParameterMap().keySet());
    }

    @Override
    public String[] getParameterValues(String name) {
        return this.getParameterMap().get(name);
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        if (null == this.requestBody) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            IoUtil.copy(request.getInputStream(), baos);
            this.requestBody = baos.toByteArray();
        }

        final ByteArrayInputStream bais = new ByteArrayInputStream(requestBody);

        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }

            @Override
            public int read() throws IOException {
                return bais.read();
            }
        };
    }
}
