package com.example.springBootDemo.controller;

import com.example.springBootDemo.config.system.session.RespBean;
import com.example.springBootDemo.entity.SendVo;
import com.example.springBootDemo.msg.SendMsgHandle;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @所属模块<p>
 * @描述<p>
 * @创建人 xiaoYe
 * @创建时间 2021/2/26 18:19
 * @Copyright (c) 2020 inc. all rights reserved<p>
 * @公司名称
 */
@Slf4j
@RestController
@Api(tags = {"推送模块"})
@RequestMapping("send")
public class SendController {

//  TODO 消息发送控制类
//  信息模板表
//  信息日志表
//  信息发送
//    ISendMethod sendMsgHandle;


    @ApiOperation(value = "获取发送信息内容")
    @PostMapping(name = "获取发送信息内容", value = "getSendContent")
    public void getSendContent() {
//      1.获取信息模板表
//      2.替换信息内容
    }

    @ApiOperation(value = "信息发送")
    @PostMapping(value = "sendMsg", name = "信息发送")
    public RespBean sendMsg(@Valid @RequestBody SendVo sendVo) {
        SendVo.SendTypeEnum sendTypeEnum =SendVo.SendTypeEnum.valueOf(sendVo.getTypeCode());
        sendVo.setSendEnum(sendTypeEnum);
        SendMsgHandle sendMsgHandle = new SendMsgHandle(sendVo);
        return RespBean.success(sendMsgHandle.sendMsg());
    }

}
