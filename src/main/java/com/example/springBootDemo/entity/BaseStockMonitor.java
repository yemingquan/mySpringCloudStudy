package com.example.springBootDemo.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.example.springBootDemo.config.components.constant.DateConstant;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;


/**
 * 股票监管池(BaseStockMonitor)实体类
 *
 * @author makejava
 * @since 2023-10-21 19:50:07
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "股票监管池")
@TableName("base_stock_monitor")
public class BaseStockMonitor implements Serializable {
    private static final long serialVersionUID = 311337783744372264L;
    /**
     * 主键
     */
    @Excel(name = "主键")
    @TableId(type = IdType.NONE)
    private Integer id;
    /**
     * 股票编码
     */
    @Excel(name = "代码")
    private String stockCode;
    /**
     * 股票名称
     */
    @Excel(name = "姓名")
    private String stockName;
    /**
     * 主业
     */
    @Excel(name = "主业")
    private String business;
    /**
     * 监控原因
     */
    @Excel(name = "监控原因", width = 23)
    private String reason;
    /**
     * 监控开始
     */
    @Excel(name = "开始时间", format = DateConstant.DATE_FORMAT_10, width = 14)
    private Date monitorStart;
    /**
     * 监控结束
     */
    @Excel(name = "结束时间", format = DateConstant.DATE_FORMAT_10, width = 14)
    private Date monitorEnd;
    /**
     * 周期
     */
    @Excel(name = "周期")
    private Integer cycle;
    /**
     * 说明
     */
    @Excel(name = "说明")
    private String instructions;


}

