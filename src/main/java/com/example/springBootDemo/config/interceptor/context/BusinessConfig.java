package com.example.springBootDemo.config.interceptor.context;

import lombok.Data;

import java.util.Map;

/**
 * @所属模块<p>
 * @描述<p>
 * @创建人 xiaoYe
 * @创建时间 2025-3-2 10:45
 * @Copyright (c) 2020 inc. all rights reserved<p>
 * @公司名称
 */
@Data
public class BusinessConfig {

    /**
     * 是否上线
     * ture-活跃
     */
    private Boolean isActive;
    /**
     * 是否加解密
     * false-不加密
     */
    private Boolean isEncrypt;

    /**
     * 对方提供的公钥，用于加密
     */
    private String publicKey;

    /**
     * 我方生成的私钥，用于解密
     */
    private String privateKey;

    /**
     * 原始报文
     * TODO 可以考虑放到下面的Map中去
     */
    private String originRequest;

    /**
     * 线程数据
     * originRequest -业务对象
     */
    private Map<String,Object> ThreadMap;

    /**
     * 区分标记-app
     * 不同项目会使用不同的区分方式
     * 比如 渠道、产品、app
     */
    private String appId;

    private String productId;

    private String channelNo;
}
