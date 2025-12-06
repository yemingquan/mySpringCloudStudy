package com.example.springBootDemo.test.websocket;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @所属模块<p>
 * @描述<p>
 *     Socket服务端：监听端口，处理客户端连接和消息
 * @创建人 xiaoYe
 * @创建时间 2025-12-6 19:27
 * @Copyright (c) 2020 inc. all rights reserved<p>
 * @公司名称
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class TcpSocketServer {
    private final ThreadPoolTaskExecutor mdcExecutor;
    // 监听端口
    private int PORT = 8888;
    // 标记服务是否运行
    private Boolean isRunning = true;


    /**
     * 为每一个服务端连接创建一个线程
     */
    public void start() {
        try (ServerSocket server = new ServerSocket(PORT)) {
            log.info("Socket服务端已启动，监听端口：" + PORT);
            while (true) {
                if (isRunning) {
                    Socket clientSocket = server.accept();
                    log.info("客户端连接成功：" + clientSocket.getInetAddress());
                    //处理单个客户端的信息交互
                    mdcExecutor.execute(() -> doBusiness(clientSocket));
                }
            }
        } catch (IOException e) {
            log.error("Socket服务端异常");
        }


    }

    /**
     * 处理单个客户端的信息交互
     * 打印信息（需要监听退出信息）
     *
     * @param clientSocket
     */
    private void doBusiness(Socket clientSocket) {
        try (
                // 输入流：读取客户端消息
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                // 输出流：向客户端响应消息
                OutputStreamWriter out = new OutputStreamWriter(clientSocket.getOutputStream());
        ) {
            String msg;
            // 循环读取客户端消息（客户端关闭则返回null）
            while ((msg = in.readLine()) != null) {
                log.info(msg);

                // 服务端响应（模拟业务处理）
                String returnMsg = "接受：" + msg + " | 时间：" + System.currentTimeMillis();
                // 换行符作为消息结束标识
                out.write(returnMsg + "\n");
                out.flush();

                // 特殊指令：客户端发送exit则关闭连接
                if ("exit".equalsIgnoreCase(msg)) {
                    log.info("客户端断开连接");
                    break;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                // 关闭客户端连接
                clientSocket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 停止Socket服务
     */
    public void shutdown() {
        log.info("Socket服务端已停止");
        isRunning = false;
    }
}
