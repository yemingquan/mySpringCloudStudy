package com.example.springBootDemo.init;

import com.example.springBootDemo.test.websocket.TcpSocketServer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

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
@Order(2)
public class SystemInit2 implements CommandLineRunner {

    private final TcpSocketServer tcpSocketServer;
    private final ThreadPoolTaskExecutor mdcExecutor;

    @Override
    public void run(String... args) throws Exception {
//         系统启动后执行的逻辑
        log.info("Application started with arguments: " + String.join(", ", args));
    }

//    @PostConstruct
//    public void startTcpSocketServer() {
//        mdcExecutor.execute(tcpSocketServer::start);
//    }
//
//    @PreDestroy
//    public void stopTcpSocketServer() {
//        tcpSocketServer.shutdown();
//    }

}
