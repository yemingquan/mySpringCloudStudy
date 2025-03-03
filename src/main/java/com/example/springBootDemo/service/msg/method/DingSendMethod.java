package com.example.springBootDemo.service.msg.method;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.example.springBootDemo.service.msg.ISendMethod;
import com.google.common.collect.Lists;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @所属模块<p>
 * @描述<p>
 * @创建人 xiaoYe
 * @创建时间 2025-3-1 20:45
 * @Copyright (c) 2020 inc. all rights reserved<p>
 * @公司名称
 */
@Component
@Slf4j
public class DingSendMethod implements ISendMethod {
    @Value("${spring.ding.url}")
    private String url;
    @Value("${spring.ding.token}")
    private String token;


    @Override
    public Boolean sendMsg(String receiver, String title, String content) {
        SendBody sendBody = SendBody.builder()
                .msgtype(SendTypeEnum.markdown.name())
                .markdown(Markdown.builder().title(title).text(content).build())
                .at(At.builder().atMobiles(Lists.newArrayList(receiver)).build())
                .build();
        log.info("返回req{}", JSONUtil.toJsonStr(sendBody));
        String res = HttpUtil.post(url + token, JSONUtil.toJsonStr(sendBody), 60 * 1000);
        log.info("返回res{}", res);
        return true;
    }


    @Getter
    @AllArgsConstructor
    @Slf4j
    public enum SendTypeEnum {

        text("文本"),
        link("链接Link类型"),
        markdown("Markdown类型"),
        ;

        private String desc;
    }


    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Data
    public static class SendBody {
        /**
         * 消息类型
         */
        private String msgtype;
        /**
         * at谁
         */
        private At at;
        /**
         * 文本消息
         */
        private Text text;
        /**
         * 链接内容
         */
        private Link link;
        /**
         * 富文本内容
         */
        private Markdown markdown;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Data
    public static class Text {
        /**
         * 文本
         */
        private String content;

    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Data
    public static class Link {
        /**
         * 文本
         */
        private String text;
        /**
         * 标题
         */
        private String title;
        /**
         * 图片地址
         */
        private String picUrl;
        /**
         * 链接地址
         */
        private String messageUrl;


    }


    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Data
    public static class Markdown {
        private String title;
        private String text;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Data
    public static class At {
        private List<String> atMobiles;
        private List<String> atUserIds;
        private Boolean isAtAll;
    }
}



