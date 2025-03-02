package com.example.springBootDemo.enums.system;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

/**
 * @所属模块<p>
 * @描述<p>
 * @创建人 xiaoYe
 * @创建时间 2021/2/26 20:08
 * @Copyright (c) 2020 inc. all rights reserved<p>
 * @公司名称
 */
@Getter
@AllArgsConstructor
@Slf4j
public enum NewsEnum {



    TYPE_FUTURE("TYPE", "1", "预期"),
    TYPE_INSTANTLY("TYPE","2", "兑现"),

    HAPPEN_BEFORE("HAPPEN","1", "开盘前"),
    HAPPEN_INSIDE("HAPPEN","2", "盘中"),
    HAPPEN_AFTER("HAPPEN","3", "盘后"),

    SCOPE_UNKNOW("SCOPE","0", "未定义"),
    SCOPE_ENVIRONMENT("SCOPE","1", "大环境"),
    SCOPE_MAIN("SCOPE","2", "主线"),
    SCOPE_ACTIVE("SCOPE","3", "活跃板块"),
    SCOPE_OTHER("SCOPE","4", "其他");

    @Getter
    @Setter
    private String type;

    @Getter
    @Setter
    private String code;

    @Getter
    @Setter
    private String name;

    public static String getName(String type, String code) {
        NewsEnum en = Arrays.stream(values())
                .filter(e -> e.getType().equals(type))
                .filter(e -> e.getCode().equals(code))
                .findFirst().orElse(null);
        return en == null ? code : en.getName();
    }

    public static String getCode(String type, String name) {
        NewsEnum en = Arrays.stream(values())
                .filter(e -> e.getType().equals(type))
                .filter(e -> e.getName().equals(name))
                .findFirst().orElse(null);
        return en == null ? name : en.getCode();
    }

}
