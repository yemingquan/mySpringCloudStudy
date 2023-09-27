package com.example.springBootDemo.msg;

import com.example.springBootDemo.config.components.enums.SendTypeEnum;
import com.example.springBootDemo.entity.SendVo;
import com.example.springBootDemo.util.SpringContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

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
    ISendMethod sendMethod;

    SendVo sendVo;

    public SendMsgHandle(SendVo sendVo) {
        this.sendVo = sendVo;
        String sendType = sendVo.getSendType();
        if (StringUtils.isBlank(sendType)) {
            return;
        }
        String sendBean = SendTypeEnum.getBeanName(sendType);
        try {
            sendMethod = SpringContextUtil.getBean(Class.forName(sendBean));
        } catch (ClassNotFoundException e) {
            return;
        }
    }

    public Boolean sendMsg() {
        if (sendMethod == null) {
            log.info("未配置发送类型[{}]", sendVo);
            return false;
        }
//      发送方式以及内容打印
        String sendName = SendTypeEnum.getName(sendVo.getSendType());
        log.info("发送方式：{}，接受者：{}，标题{}，内容{}", sendName, sendVo.getReceiver(), sendVo.getTitle(), sendVo.getContent());
        return sendMethod.sendMsg(sendVo.getReceiver(), sendVo.getTitle(), sendVo.getContent());
    }

}
