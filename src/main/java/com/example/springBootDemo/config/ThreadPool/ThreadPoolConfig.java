package com.example.springBootDemo.config.ThreadPool;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @所属模块<p>
 * @描述<p>
 * @创建人 xiaoYe
 * @创建时间 2025-3-5 23:51
 * @Copyright (c) 2020 inc. all rights reserved<p>
 * @公司名称
 */
@Configuration
public class ThreadPoolConfig {

    //TODO 参数需要定制化
    @Bean(name ="mdcExecutor")
    public MdcThreadPoolTaskExecutor threadPoolTaskExecutor() {
        MdcThreadPoolTaskExecutor executor = new MdcThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(100);
        executor.setKeepAliveSeconds(60);
        executor.setThreadNamePrefix("mdc-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        executor.initialize();
        return executor;
    }


    @Bean(name ="fixThreadPool")
    public MdcThreadPoolExecutor fixThreadPool() {
        return new MdcThreadPoolExecutor(3,3,0L, TimeUnit.MICROSECONDS,new LinkedBlockingQueue<Runnable>());
    }
}
