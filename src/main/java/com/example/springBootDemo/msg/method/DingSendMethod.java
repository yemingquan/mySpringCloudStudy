package com.example.springBootDemo.msg.method;

import cn.hutool.http.HttpUtil;
import com.example.springBootDemo.msg.ISendMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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
        String res = HttpUtil.post(url + token, content, 60 * 1000);
        log.info("返回res{}", res);
        return true;
    }


    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Data
    public class SendBody {
        private String msgType;
        private MarkDown markDown;


    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Data
    public class MarkDown {
        private String title;
        private String text;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Data
    public class At {
        private List<String> atMobiles;
        private List<String> atUserIds;
        private Boolean isAtAll;
    }
}



