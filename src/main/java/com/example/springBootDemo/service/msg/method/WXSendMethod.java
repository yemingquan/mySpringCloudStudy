package com.example.springBootDemo.service.msg.method;

import com.example.springBootDemo.service.msg.ISendMethod;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @所属模块<p>
 * @描述 微信机器人发送信息<p>
 * @创建人 xiaoYe
 * @创建时间 2021/2/26 18:12
 * @Copyright (c) 2020 inc. all rights reserved<p>
 * @公司名称
 */
@Slf4j
@Component
public class WXSendMethod implements ISendMethod {

    @Override
    public Boolean sendMsg(String receiver, String title, String content) {
//        TODO 发送微信信息
        log.info("发送微信");
        return true;
    }
}
