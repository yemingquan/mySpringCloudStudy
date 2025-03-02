package com.example.springBootDemo.config.exception;

import com.example.springBootDemo.config.exception.enums.SystemEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @所属模块<p>
 * @描述<p>
 * @创建人 xiaoYe
 * @创建时间 2025-3-1 23:46
 * @Copyright (c) 2020 inc. all rights reserved<p>
 * @公司名称
 */
@RestControllerAdvice
@Slf4j
@Order(1)
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public Object HandlerException(Exception ex) {
        try {
            Exception rootEx = getRootCause(ex);
            return handleException(rootEx);
        } catch (Exception e) {
            return "全局异常处理失败";
        }
    }

    private Object handleException(Exception ex) {
        //系统异常
        SystemEnum systemEnum = SystemEnum.formatException(ex, "0");
        //业务异常
        if (systemEnum != null) {
            String code = systemEnum.getCode();
            String msg = systemEnum.getMsg();
            // TODO 根据异常返回处理
            log.error("TODO systemEnum 未处理{}", ex.getMessage());
            return ex;
        } else if (ex.getClass().isInstance(BizException.class)) {
            // TODO 根据异常返回处理
            log.error("TODO BizException 未处理{}", ex.getMessage());
            return ex;
        } else {
            //其他自定义异常
            log.error("其他自定义异常 未处理 {}",ex.getMessage());
            // TODO  获取渠道号
            // TODO 根据渠道好获取处理类
            return ex;
        }
    }

    private Exception getRootCause(Throwable throwable) {
        Throwable rootCause = throwable.getCause();
        while (rootCause != null && rootCause.getCause() != null) {
            rootCause = rootCause.getCause();
        }
        if (rootCause instanceof Exception) {
            return (Exception) rootCause;
        } else {
            return (Exception) throwable;
        }
    }
}
