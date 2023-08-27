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
 * 题材配置化(BaseSubject)实体类
 *
 * @author makejava
 * @since 2023-08-26 15:36:36
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "题材配置化")
@TableName("base_subject")
public class BaseSubject implements Serializable {
    private static final long serialVersionUID = -49845591799490370L;
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 题材名称 A-B，比如汇率-下降
     */
    private String subName;
    /**
     * 背景说明，用于分析模式
     */
    private String backGround;
    /**
     * 说明，对市场的影响，喜好的分析
     */
    private String instructions;
    /**
     * 开始持续时间
     */
    private Date durationStart;
    /**
     * 开始持续时间
     */
    private Date durationEnd;
    /**
     * 最高板
     */
    private Integer combo;
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


}

