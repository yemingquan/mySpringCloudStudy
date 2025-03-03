package com.example.springBootDemo.config.AOP;

import cn.hutool.json.JSONUtil;
import com.example.springBootDemo.config.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * @所属模块<p>
 * @描述<p>
 * @创建人 xiaoYe
 * @创建时间 2025-3-3 22:27
 * @Copyright (c) 2020 inc. all rights reserved<p>
 * @公司名称
 */
@Slf4j
@Aspect
@Component
public class APILogAOP {

    @Around("@annotation(com.example.springBootDemo.config.AOP.LogAnnotation)")
    public Object doBefore(ProceedingJoinPoint joinPoint) throws Throwable {
        Object returnValue = "";
        try {
            Object[] args = joinPoint.getArgs();
            returnValue = joinPoint.proceed(args);
        } catch (BizException e) {
            returnValue = "returnValue BizException";
            throw e;
        } catch (Throwable e) {
            returnValue = "returnValue Throwable";
            throw e;
        } finally {
            printLog(joinPoint, returnValue);
        }

        return returnValue;
    }

    private void printLog(ProceedingJoinPoint joinPoint, Object returnValue) {
        try {
            MethodSignature joinPointObject = (MethodSignature) joinPoint.getSignature();
            Method method = joinPointObject.getMethod();
            LogAnnotation annotation = method.getAnnotation(LogAnnotation.class);
            //TODO 这个字符串可以异动到常理类中去
            String suffix = "|";
            StringBuilder builder = new StringBuilder("接口名：").append(annotation.name()).append(suffix);

            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            builder.append("接口地址=").append(request.getRequestURI()).append(suffix);
            builder.append("接口方法=").append(joinPoint.getSignature().getDeclaringTypeName()).append(".").append(joinPoint.getSignature().getName()).append(suffix);
            builder.append("请求报文=").append(JSONUtil.toJsonStr(joinPoint.getArgs()[0])).append(suffix);
            if (returnValue != null) {
                builder.append("响应报文=").append(JSONUtil.toJsonStr(returnValue)).append(suffix);
            } else {
                builder.append("响应报文为空");
            }
            log.info(builder.toString());
        } catch (Exception e) {
            log.error("AOP 处理异常", e);
        }
    }
}
