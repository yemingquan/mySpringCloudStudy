package com.example.springBootDemo.config.filter.wrapper;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @所属模块<p>
 * @描述<p>
 * @创建人 xiaoYe
 * @创建时间 2025-3-2 1:19
 * @Copyright (c) 2020 inc. all rights reserved<p>
 * @公司名称
 */
public class ResponseWrapper extends HttpServletResponseWrapper {

    private HttpServletResponse response;

    private ByteArrayOutputStream baos;

    public ResponseWrapper(HttpServletResponse response) {
        super(response);
        this.response = response;
        baos = new ByteArrayOutputStream();
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        return response.getWriter();
    }

    public ServletOutputStream getOutputStream() throws IOException {
        return new SaveContextServletOutputStream(super.getOutputStream());
    }

    private class SaveContextServletOutputStream extends ServletOutputStream {

        private final ServletOutputStream delegate;

        public SaveContextServletOutputStream(ServletOutputStream delegate) {
            this.delegate = delegate;
        }

        @Override
        public boolean isReady() {
            return this.delegate.isReady();
        }

        @Override
        public void setWriteListener(WriteListener listener) {
            this.delegate.setWriteListener(listener);
        }

        @Override
        public void write(int b) throws IOException {
            baos.write(b);
        }
    }

    public byte[] getResponseBody() {
        return this.baos.toByteArray();
    }

    public void setResponse(byte[] data) {
        this.baos = new ByteArrayOutputStream();
        this.baos.write(data, 0, data.length);
    }

}
