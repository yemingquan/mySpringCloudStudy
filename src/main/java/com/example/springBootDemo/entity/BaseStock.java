package com.example.springBootDemo.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;


/**
 * 股票信息(ReportStock)实体类
 *
 * @author makejava
 * @since 2023-10-01 23:16:01
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "股票信息")
@TableName("base_stock")
public class BaseStock implements Serializable {
    private static final long serialVersionUID = 151566834644574053L;
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
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
     * 贡献度
     */
    @Excel(name = "贡献度")
    private Double contribution;
    /**
     * 流通市值,亿元
     */
    @Excel(name = "流通市值")
    @TableField(exist = false)
    private String strCirculation;

    private Double circulation;
    /**
     * 成交额
     */
    @Excel(name = "总金额")
    private Double amount;
    /**
     * 开盘涨幅
     */
    @Excel(name = "开盘涨幅")
    private Double startGains;
    /**
     * 涨幅
     */
    @Excel(name = "涨幅")
    private Double gains;
    /**
     * 实体涨幅
     */
    @Excel(name = "实体涨幅")
    private Double entitySize;
    /**
     * 振幅
     */
    @Excel(name = "振幅")
    private Double amplitude;
    /**
     * 换手
     */
    @Excel(name = "换手")
    private Double changingHands;
    /**
     * 现价
     */
    @Excel(name = "现价")
    private Double currentPrice;
    /**
     * 5日均价
     */
    @Excel(name = "5日均价")
    private Double averagePrice5;
    /**
     * 10日均价
     */
    @Excel(name = "10日均价")
    private Double averagePrice10;
    /**
     * 说明
     */
    private String instructions;
    /**
     * 状态，有效1，无效0
     */
    private String state;
    /**
     * 创建时间
     */
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


    /**
     *  为其他表读取的数据
     */
    @Excel(name = "新股上市日期")
    @TableField(exist = false)
    private Date issueDate;

    @Excel(name = "上市板块")
    @TableField(exist = false)
    private String plate;

    @Excel(name = "新股发行价格")
    @TableField(exist = false)
    private Double price;
}

