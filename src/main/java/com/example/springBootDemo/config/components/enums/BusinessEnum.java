package com.example.springBootDemo.config.components.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

/**
 * @所属模块 配置-枚举-异常枚举-业务异常
 * @描述
 * @创建人 xiaoYe
 * @创建时间
 * @备注
 */
@Getter
@AllArgsConstructor
@Slf4j
public enum BusinessEnum {
    SUCCESS("0", "success"),
    FAIL("999999", "fail"),
    SERVER_EXCEPTION("500100", "服务端异常"),
    PARAMETER_ISNULL("500101", "输入参数为空"),

    USER_NOT_EXSIST("500102", "用户不存在"),
    ONLINE_USER_OVER("500103", "在线用户数超出允许登录的最大用户限制。"),
    SESSION_NOT_EXSIST("500104", "不存在离线session数据"),
    NOT_FIND_DATA("500105", "查找不到对应数据");

    private String code;

    private String msg;

    public static String getMsg(String code) {
        BusinessEnum en = Arrays.stream(values()).filter(e -> e.getCode().equals(code)).findFirst().orElse(null);
        return en == null ? code : en.getMsg();
    }
}
