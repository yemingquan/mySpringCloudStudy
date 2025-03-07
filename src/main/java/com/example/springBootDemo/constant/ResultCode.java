package com.example.springBootDemo.constant;

/**
 * @所属模块<p>
 * @描述<p>
 * @创建人 xiaoYe
 * @创建时间 2023-8-5 9:14
 * @Copyright (c) 2020 inc. all rights reserved<p>
 * @公司名称
 */
public class ResultCode {

    /**
     * 操作成功
     */
    public static final Integer OK = 200;

    /**
     * 操作失败
     */
    public static final Integer FAIL = 500;

    /**
     * 未知错误
     */
    public static final Integer ERROR = 502;

    /**
     * 未通过token验证
     */
    public static final Integer UNAUTHORIZED = 401;

    /**
     * 无操作权限
     */
    public static final Integer NO_OPERATION_AUTHORITY = 402;

    /**
     * 服务器出现异常
     */
    public static final Integer SERVER_ERROR = 500;

    /**
     * 请求次数过于频繁
     */
    public static final Integer REQUEST_OVER_LIMIT = 502;

}