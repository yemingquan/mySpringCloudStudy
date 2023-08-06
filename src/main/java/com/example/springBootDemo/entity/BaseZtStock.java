package com.example.springBootDemo.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.example.springBootDemo.entity.base.BaseStock;
import io.swagger.annotations.ApiModel;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * base_zt_stock
 *
 * @author
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
@ApiModel(description = "")
@TableName("base_zt_stock")
public class BaseZtStock extends BaseStock implements Serializable {
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
    private Double circulation;
    /**
     * 说明
     */
    private String instructions;

    /**
     * 连板数
     */
    @Excel(name = "连板", orderNum = "7")
    private Integer combo;

    /**
     * 涨停时间
     */
    @Excel(name = "涨停时间", orderNum = "8")
    private Date hardenTime;

    /**
     * 最终涨停时间
     */
    private Date finalHardenTime;

    /**
     * 涨停类型
     */
    private String hardenType;

    /**
     * 涨停原因
     */
    private String reason;

    /**
     * 涨幅
     */
    private Double gains;

    /**
     * 昨日涨幅
     */
    @Excel(name = "昨日涨幅", orderNum = "11")
    private Double yesterdayGains;

    /**
     * 振幅
     */
    @Excel(name = "振幅", orderNum = "9")
    private Double amplitude;

    /**
     * 昨日振幅
     */
    @Excel(name = "昨日振幅", orderNum = "12")
    private Double yesterdayAmplitude;

    /**
     * 换手
     */
    @Excel(name = "换手", orderNum = "10")
    private Double changingHands;

    /**
     * 昨日换手
     */
    @Excel(name = "昨日换手", orderNum = "13")
    private Double yesterdayChangingHands;

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
     * 备注说明
     */
    private String remark;

    private static final long serialVersionUID = 1L;
}