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
 * 连板梯队(BaseCombo)实体类
 *
 * @author makejava
 * @since 2023-10-08 13:46:13
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "连板梯队")
@TableName("base_combo")
public class BaseCombo implements Serializable {
    private static final long serialVersionUID = 328629638904458298L;
    /**
     * 日期
     */
    @TableId(type = IdType.INPUT)
    private Date date;
    /**
     * 涨停数
     */
    private Integer sumCount;
    /**
     * 涨停数
     */
    private Integer ztCount;
    /**
     * 涨停回封数
     */
    private Integer zthfCount;
    /**
     * 1b涨停数
     */
    private Integer combo1Count;
    /**
     * 2b涨停数
     */
    private Integer combo2Count;
    /**
     * 3b涨停数
     */
    private Integer combo3Count;
    /**
     * 4b涨停数
     */
    private Integer combo4Count;
    /**
     * 5b涨停数
     */
    private Integer combo5Count;
    /**
     * 1b
     */
    private String combo1;
    /**
     * 2b
     */
    private String combo2;
    /**
     * 3b
     */
    private String combo3;
    /**
     * 4b
     */
    private String combo4;
    /**
     * 5b
     */
    private String combo5;
    /**
     * 最高板
     */
    private Integer maxCombo;
    /**
     * 最高板信息
     */
    private String maxInfo;
    /**
     * 备注
     */
    private String remark;


}

