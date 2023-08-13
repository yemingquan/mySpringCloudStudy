package com.example.springBootDemo.entity;

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
 * 可转债(BaseBond)实体类
 *
 * @author makejava
 * @since 2023-08-13 23:45:20
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "可转债")
@TableName("base_bond")
public class BaseBond implements Serializable {
    private static final long serialVersionUID = 187030911648795578L;
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 转债代码
     */
    @Excel(name = "代码")
    private String tsCode;
    /**
     * 转债名称
     */
    @Excel(name = "名称")
    private String bondFullName;
    /**
     * 涨幅
     */
    @Excel(name = "涨幅")
    private Double gains;
    /**
     * 现价
     */
    @Excel(name = "现价")
    private Double par;
    /**
     * 正股简称
     */
    @Excel(name = "正股简称")
    private String stkShortName;
    /**
     * 正股价格
     */
    @Excel(name = "正股价格")
    private Double stockPrice;
    /**
     * 转股价格
     */
    @Excel(name = "转股价格")
    private Double stockPar;
    /**
     * 转股价值
     */
    @Excel(name = "转股价值")
    private Double convPrice;
    /**
     * 转股溢价
     */
    @Excel(name = "转股溢价")
    private Double cbOverRate;
    /**
     * 纯债溢价
     */
    @Excel(name = "纯债溢价")
    private Double bondOverRate;
    /**
     * 振幅
     */
    @Excel(name = "振幅")
    private Double amplitude;
    /**
     * 套利空间
     */
    @Excel(name = "套利空间")
    private Double arbitrageSpace;
    /**
     * 转股比例
     */
    @Excel(name = "转股比例")
    private Double convRate;
    /**
     * 债券余额（万元）
     */
    @Excel(name = "债券余额（万元）")
    private Double remainSize;
    /**
     * 债券规模（亿元）
     */
    @Excel(name = "债券规模（亿元）")
    private Double issueSize;
    /**
     * 到期日期
     */
    @Excel(name = "到期日期",databaseFormat="yyyyMMdd")
    private Date maturityDate;
    /**
     * 转股起始日
     */
    @Excel(name = "转股起始日",databaseFormat="yyyyMMdd")
    private Date convStartDate;
    /**
     * 强赎触发价
     */
    @Excel(name = "强赎触发价")
    private Double callPrice;
    /**
     * 回售触发价
     */
    @Excel(name = "回售触发价")
    private Double repoPrice;
    /**
     * 上市日期
     */
    private Date listDate;
    /**
     * 实体涨幅
     */
    private Double entitySize;
    /**
     * 所属板块
     */
    private String plate;
    /**
     * 分支
     */
    private String nicheBusiness;
    /**
     * 类型
     */
    private String trendType;
    /**
     * 说明
     */
    private String instructions;
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


}

