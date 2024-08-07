package com.example.springBootDemo.entity.base;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotations.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;


/**
 * 曾炸板的股票(BaseReportStock)实体类
 *
 * @author makejava
 * @since 2023-08-06 10:57:53
 */
@Data
//@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaseReportStock implements Serializable {
    private static final long serialVersionUID = -54210230650774023L;
//    /**
//     * 主键
//     */
//    private Integer id;
    /**
     * 股票编码
     */
    private String stockCode;
    /**
     * 股票名称
     */
    private String stockName;
    /**
     * 所属板块
     */
    private String plate;
    /**
     * 主业
     */
    private String mainBusiness;
    /**
     * 分支
     */
    private String nicheBusiness;
    /**
     * 流通市值,亿元
     */
    private Double circulation;

    @Excel(name = "流通市值")
    @TableField(exist = false)
    private String strCirculation;

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
    private Date touchTime;
    /**
     * 昨日涨幅
     */
    private Double yesterdayGains;
    /**
     * 开盘涨幅
     */
    private Double startGains;
    /**
     * 涨幅
     */
    private Double gains;
    /**
     * 振幅
     */
    private Double amplitude;
    /**
     * 换手
     */
    private Double changingHands;
    /**
     * 昨日振幅
     */
    private Double yesterdayAmplitude;
    /**
     * 昨日换手
     */
    private Double yesterdayChangingHands;
    /**
     * 10日均线
     */
    private Double averagePrice10;
    /**
     * 5日均线
     */
    private Double averagePrice5;
    /**
     * 现价
     */
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
    /**
     * 类型:1 涨停 2 涨停回封 3炸板 4跌停 5向上波动 6向下波动
     */
    @TableField(exist = false)
    private String source;

    private Double entitySize;

    @TableField(exist = false)
    private Double yestedayEntitySize;

    /**
     * 可转债
     */
    @TableField(exist = false)
    private String bond;

    /**
     * 可转债说明
     */
    @TableField(exist = false)
    private String bondInstructions;

    //    @Excel(name = "涨停类型")
    private String hardenType;

//    @Excel(name = "首次时间",dateFormat="HH:mm:ss")
    private Date hardenTime;

//    @Excel(name = "最终时间" ,dateFormat="HH:mm:ss")
    private Date finalHardenTime;

    /**
     * 是否次新
     */
    @TableField(exist = false)
    private String cxFlag;
}

