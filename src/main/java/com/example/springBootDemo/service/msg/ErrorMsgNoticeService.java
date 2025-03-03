package com.example.springBootDemo.service.msg;

import com.example.springBootDemo.entity.SendVo;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @所属模块<p>
 * @描述<p>
 * @创建人 xiaoYe
 * @创建时间 2025-3-3 23:24
 * @Copyright (c) 2020 inc. all rights reserved<p>
 * @公司名称
 */
@Service
public class ErrorMsgNoticeService {

    public void ordinaryExceptionSend(String title, LocalDateTime start, Throwable ex) {
        title = title + ExceptionUtils.getMessage(ex);
//        String localtion = ExceptionUtils.getMessage(ex);
        String[] msgs = ExceptionUtils.getRootCauseStackTrace(ex);
        String message = msgs.toString();
        StringBuilder builder = new StringBuilder();

        for (String str : msgs) {
            if (str.contains("com.example")) {
                message = message + str + "<br/>\n";
            }
        }

        if (message.length() > 1000) {
            message = message.substring(0, 1000);
        }

        builder.append("#### 【%s】\n")
//                .append("所在位置：<font color='red'>%s</font>\n")
                //TODO traceId
//                .append(String.format("*traceId:<font color='gray'>%s</font>\n","traceId"))
//                .append("")//时间起
//                .append("")//时间止
//                .append("")//耗时
                .append("* 失败信息：<font color='gray'>%s</font>\n");

        String errorContent = String.format(builder.toString(), title, message);

        SendVo sendVo = SendVo.builder().sendEnum(SendVo.SendTypeEnum.DD).title(title).content(errorContent).receiver("xy").build();
        SendMsgHandle sendMsgHandle = new SendMsgHandle(sendVo);
        sendMsgHandle.sendMsg();
    }
}
