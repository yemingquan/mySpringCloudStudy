package com.example.springBootDemo.entity.report;

import com.example.springBootDemo.entity.base.BaseReportStock;
import com.example.springBootDemo.util.excel.Excel;
import lombok.*;
import org.apache.poi.ss.usermodel.IndexedColors;

import java.io.Serializable;


/**
 * 波动报表
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class BdReport extends BaseReportStock implements Serializable {

    private String id;
//    @Excel(name = "类型")
//    private String reason;

//    @Excel(name = "上市板块")
//    private String plate;

    @Excel(name = "所属行业", color = IndexedColors.RED)
    private String mainBusiness;

    @Excel(name = "细分")
    private String nicheBusiness;

    @Excel(name = "代码")
    private String stockCode;

    @Excel(name = "名称", bold = true)
    private String stockName;

    @Excel(name = "说明")
    private String instructions;

    @Excel(name = "实体涨幅", suffix = "%", bold = true)
    private Double entitySize;

    @Excel(name = "昨日涨幅", suffix = "%")
    private Double yesterdayGains;

    @Excel(name = "开盘涨幅", suffix = "%", bold = true)
    private Double startGains;

    @Excel(name = "涨幅", suffix = "%", bold = true)
    private Double gains;

    @Excel(name = "流通市值")
    private Double circulation;

    @Excel(name = "振幅", suffix = "%")
    private Double amplitude;

    @Excel(name = "换手", suffix = "%")
    private Double changingHands;

    @Excel(name = "昨日振幅", suffix = "%")
    private Double yesterdayAmplitude;

    @Excel(name = "昨日换手", suffix = "%")
    private Double yesterdayChangingHands;

    @Excel(name = "可转债")
    private String bond;

    @Excel(name = "可转债说明")
    private String bondInstructions;

    private static final long serialVersionUID = 1L;
}