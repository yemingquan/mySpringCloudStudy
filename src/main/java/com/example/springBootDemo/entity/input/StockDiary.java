package com.example.springBootDemo.entity.input;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "股票日记")
public class StockDiary implements Serializable {
    private static final long serialVersionUID = 151566834644574053L;
    /**
     * 主键
     */
    private Integer id;
    /**
     * 股票编码
     */
    @Excel(name = "代码")
    private String stockCode;
    /**
     * 股票名称
     */
    @Excel(name = "名称")
    private String stockName;
    /**
     * 说明
     */
    @Excel(name = "说明")
    private String instructions;
    /**
     * 状态，有效1，无效0
     */
    private String state;
    /**
     * 创建时间
     */
    @Excel(name = "日期")
    private Date createDate;
    /**
     * 创建者
     */
    private String createBy;
    /**
     * 修改时间
     */
    private Date modifedDate;
    /**
     * 修改者
     */
    private String modifedBy;

}

