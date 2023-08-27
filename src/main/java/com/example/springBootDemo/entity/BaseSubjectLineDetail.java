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
 * 题材线明细(BaseSubjectLineDetail)实体类
 *
 * @author makejava
 * @since 2023-08-25 17:39:58
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "题材线明细")
@TableName("base_subject_line_detail")
public class BaseSubjectLineDetail implements Serializable {
    private static final long serialVersionUID = -63101766358407601L;
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 题材线名称，比如开开实业-主升
     */
    private String subLineName;
    /**
     * 主要行业或结构
     */
    private String mainBusiness;
    /**
     * 核心名称;分隔
     */
    private String coreName;
    /**
     * 跟风名称;分隔
     */
    private String helpName;
    /**
     * 涨停数
     */
    private Integer countZt;
    /**
     * 回封数
     */
    private Integer countZthf;
    /**
     * 出现的模式 ;分隔
     */
    private String model;
    /**
     * 说明，当天板块和市场的情况。核心的表现，助攻的表现
     */
    private String instructions;
    /**
     * 预期
     */
    private String expect;
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
     * 备注
     */
    private String remark;


}

