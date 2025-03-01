package com.example.springBootDemo.config.components.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;


/**
 * @所属模块 配置-枚举-异常枚举-系统异常
 * @描述
 * @创建人 xiaoYe
 * @创建时间
 * @备注
 */
@Getter
@AllArgsConstructor
@Slf4j
public enum ErrorEnum {
    SUCCESS("000", "操作成功"),
    FAIL("999", "操作失败");


    private String code;


    private String msg;


    public static String getMsg(String code) {
        ErrorEnum en = Arrays.stream(values()).filter(e -> e.getCode().equals(code)).findFirst().orElse(null);
        return en == null ? code : en.getMsg();
    }
}