package com.example.springBootDemo.config.exception;

import com.example.springBootDemo.config.exception.enums.ErrorEnum;
import lombok.Data;

/**
 * @所属模块 配置-异常控制-业务异常
 * @描述 业务异常类
 * @创建人 xiaoYe
 * @创建时间 2020/5/20 17:21
 * @备注
 */
@Data
public class BizException extends RuntimeException {
    private String code;
    private String msg;

    public BizException(ErrorEnum e) {
        super(e.getMsg());
        setCode(e.getCode());
        setMsg(e.getMsg());
    }

    public BizException(String msg) {
        super(msg);
        setCode(ErrorEnum.FAIL.getCode());
        setMsg(msg);
    }

    public BizException(ErrorEnum e, String msg) {
        super(msg);
        setCode(e.getCode());
        setMsg(msg);
    }

    public BizException(String msg, Object... args) {
        super(getMsg(msg, args));
        setCode(ErrorEnum.FAIL.getCode());
        setMsg(getMsg(msg, args));
    }

    private static String getMsg(String msg, Object... args) {
        for (int i = 0; i < args.length; i++) {
            msg = msg.replace("{" + i + "}", args[i] + "");
        }

        return msg;
    }

    public BizException(ErrorEnum e, String msg, Object... args) {
        super(getMsg(msg, args));
        setCode(e.getCode());
        setMsg(getMsg(msg, args));
    }
}
