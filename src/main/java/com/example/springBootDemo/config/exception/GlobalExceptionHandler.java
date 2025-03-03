package com.example.springBootDemo.config.exception;

import com.example.springBootDemo.config.exception.enums.SystemEnum;
import com.example.springBootDemo.service.msg.ErrorMsgNoticeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

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
    @Autowired
    private ErrorMsgNoticeService errorMsgNoticeService;

    @ExceptionHandler(Exception.class)
    public Object HandlerException(Exception ex) {
        try {
            Exception rootEx = getRootCause(ex);
            return handleException(rootEx);
        } catch (Exception e) {
            return "内部异常";
        }
    }

    private Object handleException(Exception ex) {
        log.error("全局异常处理失败", ex);
        //系统异常
        SystemEnum systemEnum = SystemEnum.formatException(ex, "0");
        //业务异常
        if (systemEnum != null) {
            String code = systemEnum.getCode();
            String msg = systemEnum.getMsg();
            // TODO 根据异常返回处理
            log.error("TODO systemEnum 未处理{}", ex.getMessage());
            return "系统异常";
        } else if (ex.getClass().isInstance(BizException.class)) {
            // TODO 根据异常返回处理
            log.error("TODO BizException 未处理{}", ex.getMessage());
            return "业务异常";
        } else {
            //其他自定义异常
            // TODO  获取渠道号
            // TODO 根据渠道好获取处理类
            errorMsgNoticeService.ordinaryExceptionSend("全局异常处理告警", LocalDateTime.now(), ex);
            return "自定义异常";
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
