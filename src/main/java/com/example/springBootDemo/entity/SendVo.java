package com.example.springBootDemo.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @所属模块<p>
 * @描述 发送短信对象<p>
 * @创建人 xiaoYe
 * @创建时间 2021/2/26 18:37
 * @Copyright (c) 2020 inc. all rights reserved<p>
 * @公司名称
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "发送实体")
public class SendVo {

    @ApiModelProperty(value = "推送方式：1短信 2邮件 3微信", required = true, example = "2")
    @NotBlank(message = "发送类型不允许为空")
    @JsonProperty("sendType")
    private String sendType;

    @ApiModelProperty(value = "模板类型", required = false, example = "1")
    @JsonProperty("templateType")
    private String templateType;

    @ApiModelProperty(value = "接受者", required = true, example = "815129539@QQ.com")
    @NotBlank(message = "接受者不允许为空")
    @JsonProperty("receiver")
    private String receiver;

    @ApiModelProperty(value = "标题", required = false, example = "标题测试")
    @JsonProperty("title")
    private String title;

    @ApiModelProperty(value = "发送内容", required = true, example = "使用模板时用xxx=yyy;aaa=bbb格式")
    @NotBlank(message = "发送内容不允许为空")
    @JsonProperty("content")
    private String content;
}
