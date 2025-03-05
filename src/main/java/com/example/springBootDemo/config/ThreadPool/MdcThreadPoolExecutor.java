package com.example.springBootDemo.config.ThreadPool;

import com.example.springBootDemo.util.SerialUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import java.util.Map;
import java.util.concurrent.*;

/**
 * @所属模块<p>
 * @描述<p>
 * @创建人 xiaoYe
 * @创建时间 2025-3-6 0:28
 * @Copyright (c) 2020 inc. all rights reserved<p>
 * @公司名称
 */
@Slf4j
public class MdcThreadPoolExecutor extends ThreadPoolExecutor {

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

    public MdcThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    public MdcThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
    }

    public MdcThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
    }

    public MdcThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    }
}
