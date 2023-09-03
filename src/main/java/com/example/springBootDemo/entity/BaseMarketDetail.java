package com.example.springBootDemo.entity;

import java.time.LocalTime;
import java.util.Date;
import java.io.Serializable;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 盘面明细(BaseMarketDetail)实体类
 *
 * @author makejava
 * @since 2023-09-03 20:07:54
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "盘面明细")
@TableName("base_market_detail")
public class BaseMarketDetail implements Serializable {
    private static final long serialVersionUID = -13415154264375718L;
    /**
     * 主键
     */
    private Integer id;
    /**
     * 日期
     */
    private Date date;
    /**
     * 开始持续时间
     */
    private LocalTime timeStart;
    /**
     * 结束持续时间
     */
    private LocalTime timeEnd;
    /**
     * 发生时间：0竞价、1早盘-开盘部分 2早盘、3午盘、4尾盘
     */
    private String stage;
    /**
     * 趋势类型：1.指数、2。预期量能、3黄白线、4、北上、5A50/权重、0、板块
     */
    private String trendType;
    /**
     * 趋势：1。向上、震荡、向下 2。增加、横盘、减少、3.权重、重合、题材、4.流入、震荡、流出 5、0强势、洗盘（止跌/回落）、负反馈
     */
    private String trend;
    /**
     * 行业，比如：一带一路
     */
    private String mainBusiness;
    /**
     * 核心标的
     */
    private String coreName;
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
     * 备注说明
     */
    private String remark;


}

