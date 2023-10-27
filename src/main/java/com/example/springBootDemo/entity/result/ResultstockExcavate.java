package com.example.springBootDemo.entity.result;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


/**
 * 股票挖掘结果类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResultstockExcavate implements Serializable {
    private static final long serialVersionUID = 139155789162003218L;

    @Excel(name = "代码")
    private String stockCode;
    @Excel(name = "名称")
    private String stockName;
    @Excel(name = "板块")
    private String plate;
    @Excel(name = "流通(亿)",numFormat = "#.##")
    private Double circulation;

    @Excel(name = "行业", width = 30 )
    private String mainBusiness;
    @Excel(name = "热点行业", width = 76)
    private String nicheBusiness;
    @Excel(name = "属性", width = 30)
    private String attr;

    @Excel(name = "说明", width = 30)
    private String instructions;

    @Excel(name = "现价")
    private Double currentPrice;
//    @Excel(name = "5日均价")
    private Double averagePrice5;
//    @Excel(name = "10日均价")
    private Double averagePrice10;
    @Excel(name = "趋势现状", width = 15)
    private String tendency;

}

