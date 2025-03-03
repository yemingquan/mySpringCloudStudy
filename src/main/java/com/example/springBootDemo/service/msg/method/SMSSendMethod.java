package com.example.springBootDemo.service.msg.method;

import com.example.springBootDemo.service.msg.ISendMethod;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @所属模块<p>
 * @描述 发短信可以有多种策略1.api发送2.短信mao发送3.SMS短信平台<p>
 *     发送短信可以使用一个号，也可以使用一个池子<p>
 * @创建人 xiaoYe
 * @创建时间 2021/2/26 17:56
 * @Copyright (c) 2020 inc. all rights reserved<p>
 * @公司名称
 */
@Slf4j
@Component
public class SMSSendMethod implements ISendMethod {


    @Override
    public Boolean sendMsg(String receiver, String title, String content) {
//        TODO 发送短信信息
        log.info("发短信");
        return true;
    }
}
