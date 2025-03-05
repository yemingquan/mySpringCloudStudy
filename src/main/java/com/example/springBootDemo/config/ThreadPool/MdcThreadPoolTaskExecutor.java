package com.example.springBootDemo.config.ThreadPool;

import com.example.springBootDemo.util.SerialUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * @所属模块<p>
 * @描述<p>
 * @创建人 xiaoYe
 * @创建时间 2025-3-5 23:59
 * @Copyright (c) 2020 inc. all rights reserved<p>
 * @公司名称
 */
@Slf4j
public class MdcThreadPoolTaskExecutor extends ThreadPoolTaskExecutor {

    @Override
    public void execute(Runnable task) {
        log.info("异步线程MDC执行");
        Map<String, String> context = MDC.getCopyOfContextMap();
        super.execute(() -> {
            if (null != context) {
                MDC.setContextMap(context);
            } else {
                MDC.put("traceId", SerialUtil.get32UUID());
            }
            try {
                task.run();
            } finally {
                try {
                    if (null != MDC.getMDCAdapter())
                        MDC.clear();
                } catch (Exception e) {
                    log.error("MDC clear exception:{}", e.getMessage());
                }
            }
        });
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        log.info("异步线程MDC执行");
        Map<String, String> context = MDC.getCopyOfContextMap();
        return super.submit(() -> {
            if (null != context) {
                MDC.setContextMap(context);
            } else {
                MDC.put("traceId", SerialUtil.get32UUID());
            }
            try {
                return task.call();
            } finally {
                try {
                    if (null != MDC.getMDCAdapter())
                        MDC.clear();
                } catch (Exception e) {
                    log.error("MDC clear exception:{}", e.getMessage());
                }
            }
        });
    }
}
