package com.example.springBootDemo.config.exception.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;

/**
 * @所属模块<p>
 * @描述<p>
 * @创建人 xiaoYe
 * @创建时间 2025-3-2 0:08
 * @Copyright (c) 2020 inc. all rights reserved<p>
 * @公司名称
 */
@Getter
@AllArgsConstructor
@Slf4j
public enum SystemEnum {
    /**
     * 数据库相关异常
     */

    SQL_EXCEPTON(SQLException.class, "500", "SQL Exception"),

    UNKNOWN_EXCEPTION(Exception.class, "500", "An unexpected error occurred"),
    ;
    public final Class<? extends Exception> exceptionClass;
    String code;
    String msg;

    /**
     *
     * @param exception
     * @param type 1 默认异常模式 其他 null
     * @return
     */
    public static SystemEnum formatException(Exception exception, String type) {
        for (SystemEnum tempEnum : values()) {
            if (tempEnum.getExceptionClass().isInstance(exception)) {
                return tempEnum;
            }
        }

        if ("1".equals(type)) {
            return UNKNOWN_EXCEPTION;
        } else {
            return null;
        }

    }
}
