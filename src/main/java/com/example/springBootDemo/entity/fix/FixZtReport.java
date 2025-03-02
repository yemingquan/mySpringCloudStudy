package com.example.springBootDemo.entity.fix;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.example.springBootDemo.constant.DateConstant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 涨停报表
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FixZtReport implements Serializable {

    @Excel(name = "上市板块")
    private String plate;

    @Excel(name = "所属行业", width = 20)
    private String mainBusiness;

    @Excel(name = "细分", width = 30)
    private String nicheBusiness;

    @Excel(name = "代码")
    private String stockCode;

    @Excel(name = "名称")
    private String stockName;

    @Excel(name = "板")
    private Integer combo;

    @Excel(name = "首次时间", format = DateConstant.TIME_FORMAT_8, width = 20)
    private Date hardenTime;

    @Excel(name = "最终时间", format = DateConstant.TIME_FORMAT_8, width = 20)
    private Date finalHardenTime;

    private static final long serialVersionUID = 12222L;
}