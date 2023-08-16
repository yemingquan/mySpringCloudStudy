package com.example.springBootDemo.entity.report;

import com.example.springBootDemo.entity.base.BaseStock;
import com.example.springBootDemo.util.excel.Excel;
import lombok.*;
import org.apache.poi.ss.usermodel.IndexedColors;

import java.io.Serializable;
import java.util.Date;

/**
 * 摸板报表
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class MbReport extends BaseStock implements Serializable {

    @Excel(name = "上市板块")
    private String plate;

    @Excel(name = "所属行业")
    private String mainBusiness;

    @Excel(name = "细分")
    private String nicheBusiness;

    @Excel(name = "代码")
    private String stockCode;

    @Excel(name = "名称", bold = true)
    private String stockName;

    @Excel(name = "说明")
    private String instructions;

    @Excel(name = "摸板时间",dateFormat="HH:mm:ss", bold = true)
    private Date touchTime;

    @Excel(name = "实体涨幅", suffix = "%", bold = true, color = IndexedColors.RED)
    private Double entitySize;

    @Excel(name = "涨幅",suffix="%", bold = true)
    private Double gains;

    @Excel(name = "流通市值")
    private Double circulation;

    @Excel(name = "振幅",suffix="%")
    private Double amplitude;

    @Excel(name = "换手",suffix="%")
    private Double changingHands;

    @Excel(name = "昨日振幅",suffix="%")
    private Double yesterdayAmplitude;

    @Excel(name = "昨日换手",suffix="%")
    private Double yesterdayChangingHands;

//    @Excel(name = "10日均价")
//    private Double averagePrice10;
//
//    @Excel(name = "5日均价")
//    private Double averagePrice5;

    @Excel(name = "现价")
    private Double currentPrice;

    @Excel(name = "可转债")
    private String bond;

    @Excel(name = "可转债说明")
    private String bondInstructions;

    private static final long serialVersionUID = 1L;
}