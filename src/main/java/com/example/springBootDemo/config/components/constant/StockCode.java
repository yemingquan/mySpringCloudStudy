package com.example.springBootDemo.config.components.constant;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @所属模块<p>
 * @描述<p>
 * @创建人 xiaoYe
 * @创建时间 2023-8-8 17:50
 * @Copyright (c) 2020 inc. all rights reserved<p>
 * @公司名称
 */
public class StockCode {
    public static List<String> BSD_STOCK_LIST;

    {
        BSD_STOCK_LIST = Lists.newArrayList(

        );
    }

    @AllArgsConstructor
    public enum BusinessTypeEnum{
        FLAG("flag","标记"),
        INDUSTRY("industry","行业"),
        SUBJECT("subject","题材"),
        ATTR("attr(","属性");

        @Setter
        @Getter
        private String code;

        @Setter
        @Getter
        private String name;
    }
}
