package com.example.springBootDemo.entity.report;

import com.example.springBootDemo.constant.DateConstant;
import com.example.springBootDemo.util.excel.Excel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @所属模块<p>
 * @描述<p>
 * @创建人 xiaoYe
 * @创建时间 2023-10-22 21:27
 * @Copyright (c) 2020 inc. all rights reserved<p>
 * @公司名称
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ModelReport implements Serializable {
    private static final long serialVersionUID = 232;

    private String id;

    @Excel(name = "模式类型")
    private String type;

    @Excel(name = "特征")
    private String attr;

    @Excel(name = "行为")
    private String behaviour;

    @Excel(name = "模式")
    private String name;

    @Excel(name = "日期",dateFormat= DateConstant.DATE_FORMAT_10)
    private Date date;

    @Excel(name = "星期")
    private String week;

    @Excel(name = "竞价状态")
    private String bidding;

    @Excel(name = "竞价指数")
    private Double sseBidding;

    @Excel(name = "关键词")
    private String keyWords;

    @Excel(name = "市场动态")
    private String marketTrends;

    @Excel(name = "股票")
    private String stockname;

    @Excel(name = "结果")
    private String result;

    @Excel(name = "说明")
    private String instructions;
}
