package com.example.springBootDemo.config.components.constant;

import com.google.common.collect.Lists;

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
                "百利电气", "金科股份", "永鼎股份", "法尔胜",
                "云南城投", "金科股份", "荣盛发展",
                "太平洋", "首创证券", "中信证券",
                "中央商场", "国芳集团",  "人人乐",
                "鸿博股份", "金桥信息", "浪潮信息", "工业富联", "拓维信息", "中科曙光"
        );
    }

}
