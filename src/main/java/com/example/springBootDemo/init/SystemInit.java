package com.example.springBootDemo.init;

import com.example.springBootDemo.config.ThreadPool.MdcThreadPoolTaskExecutor;
import com.example.springBootDemo.test.websocket.TcpSocketServer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @所属模块<p>
 * @描述<p>
 * @创建人 xiaoYe
 * @创建时间 2025-12-6 15:36
 * @Copyright (c) 2020 inc. all rights reserved<p>
 * @公司名称
 */
@Slf4j
@RequiredArgsConstructor
@Component
@Order(1)
public class SystemInit implements ApplicationRunner {

    // 声明Socket服务端实例
    private final TcpSocketServer socketServer;

    private final Environment env;
    private final MdcThreadPoolTaskExecutor mdcExecutor;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("ApplicationRunner : " + args.toString());
        log.info("ApplicationRunner env : " + env);
    }

    /**
     * 自动启动Socket服务
     */
    @PostConstruct
    public void startSocketServer() {
        // 异步启动（避免阻塞Spring Boot主线程）
//        new Thread(socketServer::start).start();
        //方法引用
        mdcExecutor.execute(new Thread(socketServer::start));


        //无法优化成上面的样子，因为参数不一样
        mdcExecutor.execute(() -> log.info("Lambda 表达式"));
        //匿名内部类
        mdcExecutor.execute(new Runnable() {
            @Override
            public void run() {
                log.info("匿名内部类");
            }
        });
    }

    /**
     * 停止Socket服务
     */
    @PreDestroy
    public void stopSocketServer() {
        socketServer.shutdown();
    }
}
