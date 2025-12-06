package com.example.springBootDemo.test.websocket;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * @所属模块<p>
 * @描述<p> 创建socket
 * 通过控制台输入，向服务端发送信息（需要监听退出信息）
 * 读取服务端响应
 * @创建人 xiaoYe
 * @创建时间 2025-12-6 20:01
 * @Copyright (c) 2020 inc. all rights reserved<p>
 * @公司名称
 */
@Slf4j
public class TcpSocketClient {


    public static void main(String[] args) {
        int serverPort = 8888;
        String serverIp = "127.0.0.1";
        try (
                Socket socket = new Socket(serverIp, serverPort);
                OutputStreamWriter out = new OutputStreamWriter(socket.getOutputStream());
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                Scanner scanner = new Scanner(System.in);
        ) {
            log.info("已连接到服务:{}:{}", serverIp, serverPort);
            log.info("输入消息发送（输入exit断开连接）：");
            while (true) {
                String scannerMsg = scanner.nextLine();
                out.write(scannerMsg + "\n");
                out.flush();

                if ("exit".equalsIgnoreCase(scannerMsg)) {
                    log.info("客户端退出");
                    break;
                }

                //读取服务端响应
                String serverResponse = in.readLine();
                log.info("服务端响应：" + serverResponse);
            }
        } catch (IOException e) {
            log.error("客户端连接异常");
        }

    }
}
