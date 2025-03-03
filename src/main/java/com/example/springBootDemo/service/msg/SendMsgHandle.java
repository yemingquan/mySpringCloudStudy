package com.example.springBootDemo.service.msg;

import com.example.springBootDemo.entity.SendVo;
import com.example.springBootDemo.util.SpringContextUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @所属模块<p>
 * @描述<p>
 * @创建人 xiaoYe
 * @创建时间 2021/2/26 19:58
 * @Copyright (c) 2020 inc. all rights reserved<p>
 * @公司名称
 */
@Slf4j
public class SendMsgHandle {
    ISendMethod sender;

    SendVo sendVo;

    public SendMsgHandle(SendVo sendVo) {
        this.sendVo = sendVo;
        if (sendVo==null) {
            return;
        }
        try {
            sender = SpringContextUtil.getBean(Class.forName(sendVo.getSendEnum().getBeanName()));
        } catch (ClassNotFoundException e) {
            return;
        }
    }

    public Boolean sendMsg() {
        if (sender == null) {
            log.info("未配置发送类型[{}]", sendVo);
            return false;
        }
//      发送方式以及内容打印
        String sendName = sendVo.getSendEnum().getName();
        log.info("发送方式:{}，接受者:{}，标题:{}，内容:{}", sendName, sendVo.getReceiver(), sendVo.getTitle(), sendVo.getContent());
        return sender.sendMsg(sendVo.getReceiver(), sendVo.getTitle(), sendVo.getContent());
    }

}
