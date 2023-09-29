package com.example.springBootDemo.config.components.enums;

import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public enum SendTypeEnum {

    //推送方式：1短信 2邮件 3微信
    SMS("1", "短信","com.example.springBootDemo.msg.method.SMSSendMethod"),
    EMAIL("2", "邮件","com.example.springBootDemo.msg.method.EmailSendMethod"),
    WX("3", "微信","com.example.springBootDemo.msg.method.WXSendMethod");

    @Getter
    @Setter
    private String code;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private String beanName;

    public static String getBeanName(String code) {
        SendTypeEnum en = Arrays.stream(values()).filter(e -> e.getCode().equals(code)).findFirst().orElse(null);
        return en == null ? code : en.getBeanName();
    }

    public static String getName(String code) {
        SendTypeEnum en = Arrays.stream(values()).filter(e -> e.getCode().equals(code)).findFirst().orElse(null);
        return en == null ? code : en.getName();
    }
}
