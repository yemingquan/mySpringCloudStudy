package com.example.springBootDemo.msg;

/**
 * @所属模块<p>
 * @描述<p>
 * @创建人 xiaoYe
 * @创建时间 2021/2/25 11:05
 * @Copyright (c) 2020 inc. all rights reserved<p>
 * @公司名称
 */
public interface ISendMethod {

    /**
     * 发送信息
     *
     * @param receiver 接受地址
     * @param title    标题
     * @param content  内容
     */
    Boolean sendMsg(String receiver, String title, String content);
}
