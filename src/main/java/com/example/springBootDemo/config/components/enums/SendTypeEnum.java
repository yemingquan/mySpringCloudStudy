package com.example.springBootDemo.config.components.enums;

import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;

/**
 * @所属模块<p>
 * @描述<p>
 * @创建人 xiaoYe
 * @创建时间 2021/2/26 20:08
 * @Copyright (c) 2020 inc. all rights reserved<p>
 * @公司名称
 */
public enum SendTypeEnum {

    //推送方式：1短信 2邮件 3微信
    SMS("1", "com.example.springBootDemo.config.components.msg.SMSSendMethod"),
    EMAIL("2", "com.example.springBootDemo.config.components.msg.EmailSendMethod"),
    WX("3", "com.example.springBootDemo.config.components.msg.WXSendMethod");

    @Getter
    @Setter
    private String code;

    @Getter
    @Setter
    private String beanName;

    SendTypeEnum(String code, String beanName) {
        setCode(code);
        setBeanName(beanName);
    }

    public static String getBeanName(String code) {
        SendTypeEnum en = Arrays.stream(values()).filter(e -> e.getCode().equals(code)).findFirst().orElse(null);
        return en == null ? code : en.getBeanName();
    }
}
