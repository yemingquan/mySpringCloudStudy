package com.example.springBootDemo.entity.report;

import com.example.springBootDemo.entity.base.BaseStock;
import com.example.springBootDemo.util.excel.Excel;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * 涨停报表
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ZtReport extends BaseStock implements Serializable {

    @Excel(name = "类型")
    private String reason;

    @Excel(name = "上市板块")
    private String plate;

    @Excel(name = "所属行业")
    private String mainBusiness;

    @Excel(name = "细分")
    private String nicheBusiness;

    @Excel(name = "流通市值")
    private Double circulation;

    @Excel(name = "代码")
    private String stockCode;

    @Excel(name = "名称", bold = true)
    private String stockName;

    @Excel(name = "说明")
    private String instructions;

    @Excel(name = "连板")
    private Integer combo;

    @Excel(name = "首次时间",dateFormat="HH:mm:ss")
    private Date hardenTime;

    @Excel(name = "最终时间" ,dateFormat="HH:mm:ss")
    private Date finalHardenTime;

    @Excel(name = "振幅",suffix="%")
    private Double amplitude;

    @Excel(name = "换手",suffix="%")
    private Double changingHands;

    @Excel(name = "昨日涨幅",suffix="%")
    private Double yesterdayGains;

    @Excel(name = "昨日振幅",suffix="%")
    private Double yesterdayAmplitude;

    @Excel(name = "昨日换手",suffix="%")
    private Double yesterdayChangingHands;

    @Excel(name = "涨停类型")
    private String hardenType;

//    @Excel(name = "昨天实体涨幅")
    private Double yestedayEntitySize;

    //板块首版时间
    private Date SBTime;

    private static final long serialVersionUID = 1L;
}