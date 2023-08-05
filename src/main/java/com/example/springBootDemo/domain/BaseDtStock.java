package com.example.springBootDemo.domain;

import cn.afterturn.easypoi.excel.annotation.Excel;
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
 * (BaseDtStock)实体类
 *
 * @author makejava
 * @since 2023-08-06 01:18:38
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "")
@TableName("base_dt_stock")
public class BaseDtStock implements Serializable {
    private static final long serialVersionUID = -70647211829841658L;
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 股票编码
     */
    @Excel(name = "代码", orderNum = "5")
    private String stockCode;

    /**
     * 股票名称
     */
    @Excel(name = "名称", orderNum = "6")
    private String stockName;

    /**
     * 所属板块
     */
    @Excel(name = "上市板块", orderNum = "1")
    private String plate;

    /**
     * 主业
     */
    @Excel(name = "所属行业", orderNum = "2")
    private String mainBusiness;

    /**
     * 分支
     */
    @Excel(name = "细分行业", orderNum = "3")
    private String nicheBusiness;

    @Excel(name = "流通市值", orderNum = "4")
    private double circulation;
    /**
     * 说明
     */
    private String instructions;
    /**
     * 连板数
     */
    private Integer combo;
    /**
     * 摸板时间
     */
    @Excel(name = "摸板时间", orderNum = "7")
    private Date touchTime;
    /**
     * 昨日涨幅
     */
    @Excel(name = "昨日涨幅", orderNum = "8")
    private Double yesterdayGains;
    /**
     * 开盘涨幅
     */
    @Excel(name = "开盘涨幅", orderNum = "9")
    private Double startGains;
    /**
     * 涨幅
     */
    @Excel(name = "涨幅", orderNum = "10")
    private Double gains;
    /**
     * 振幅
     */
    @Excel(name = "振幅", orderNum = "11")
    private Double amplitude;
    /**
     * 换手
     */
    @Excel(name = "换手", orderNum = "12")
    private Double changingHands;
    /**
     * 昨日振幅
     */
    @Excel(name = "昨日振幅", orderNum = "13")
    private Double yesterdayAmplitude;
    /**
     * 昨日换手
     */
    @Excel(name = "昨日换手", orderNum = "14")
    private Double yesterdayChangingHands;
    /**
     * 10日均线
     */
    @Excel(name = "10日均价", orderNum = "15")
    private Double averagePrice10;
    /**
     * 5日均线
     */
    @Excel(name = "5日均价", orderNum = "16")
    private Double averagePrice5;
    /**
     * 现价
     */
    @Excel(name = "现价", orderNum = "17")
    private Double currentPrice;
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
     * 走势类型
     */
    private String trendType;
    /**
     * 备注说明
     */
    private String remark;
}

