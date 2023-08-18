package com.example.springBootDemo.entity;

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
 * 辨识度股票(ConfBsdStock)实体类
 *
 * @author makejava
 * @since 2023-08-17 18:35:09
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "辨识度股票")
@TableName("conf_bsd_stock")
public class ConfBsdStock  implements Serializable {
    private static final long serialVersionUID = 693794687279275950L;
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Integer id;
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
     * 属性
     */
    private String attr;
    /**
     * 说明
     */
    private String instructions;
    /**
     * 类型：1系统
     */
    private String type;
    /**
     * 有效时间
     */
    private Date validDate;
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
     * 备注说明
     */
    private String remark;


}

