package com.example.springBootDemo.config.components.system.session;

import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.HashMap;

/**
 * @所属模块<p>
 * @描述<p>
 * @创建人 xiaoYe
 * @创建时间 2023-8-5 9:12
 * @Copyright (c) 2020 inc. all rights reserved<p>
 * @公司名称
 */
@Slf4j
public class RespBean<T> extends HashMap<String,Object> implements Serializable {

    private static final long serialVersionUID = -4692921179644040027L;

    //状态码
    private int code;
    //返回内容
    private String msg;
    //数据对象
    private Object data;

    /**
     * 状态码
     */
    public static final String CODE_TAG = "code";

    /**
     * 返回内容
     */
    public static final String MSG_TAG = "msg";

    /**
     * 数据对象
     */
    public static final String DATA_TAG = "data";

    /**
     * 初始化一个新创建的 RespBean 对象，使其表示一个空消息。
     * @param
     */
    public RespBean() {
    }

    /**
     * 初始化一个新创建的 RespBean 对象
     *
     * @param code 状态码
     * @param msg  返回内容
     */
    public RespBean(int code, String msg) {
        super.put(CODE_TAG, code);
        super.put(MSG_TAG, msg);
        this.code=code;
        this.msg=msg;
    }

    /**
     * 初始化一个新创建的 RespBean 对象
     *
     * @param code 状态码
     * @param msg  返回内容
     * @param data 数据对象
     */
    public RespBean(int code, String msg, Object data) {
        super.put(CODE_TAG, code);
        super.put(MSG_TAG, msg);
        this.code=code;
        this.msg=msg;
        if (!(data==null)) {
            super.put(DATA_TAG, data);
            this.data=data;
        }
    }

    /**
     * 返回成功消息
     *
     * @return 成功消息
     */
    public static RespBean success() {
        return RespBean.success("操作成功!");
    }

    /**
     * 返回成功消息
     *
     * @param msg 返回内容
     * @return 成功消息
     */
    public static RespBean success(String msg) {
        log.info("返回给前端msg[{}]",msg);
        return RespBean.success(msg, null);
    }

    /**
     * 返回成功数据
     *
     * @return 成功消息
     */
    public static RespBean success(Object data) {
        return RespBean.success("操作成功!", data);
    }


    /**
     * 返回成功消息
     *
     * @param msg  返回内容
     * @param data 数据对象
     * @return 成功消息
     */
    public static RespBean success(String msg, Object data) {
        return new RespBean(200, msg, data);
    }

    /**
     * 返回错误消息
     *
     * @return
     */
    public static RespBean error() {
        return RespBean.error("操作失败!");
    }

    /**
     * 返回错误消息
     *
     * @param msg 返回内容
     * @return 警告消息
     */
    public static RespBean error(String msg) {
        return RespBean.error(msg, null);
    }

    /**
     * 返回错误消息
     *
     * @param msg  返回内容
     * @param data 数据对象
     * @return 警告消息
     */
    public static RespBean error(String msg, Object data) {
        return new RespBean(500, msg, data);
    }

    /**
     * 返回错误消息
     *
     * @param code 状态码
     * @param msg  返回内容
     * @return 警告消息
     */
    public static RespBean error(int code, String msg) {
        return new RespBean(code, msg, null);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
