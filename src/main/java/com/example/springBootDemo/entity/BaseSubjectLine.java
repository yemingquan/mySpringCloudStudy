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
 * 题材线(BaseSubjectLine)实体类
 *
 * @author makejava
 * @since 2023-08-26 15:36:17
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "题材线")
@TableName("base_subject_line")
public class BaseSubjectLine implements Serializable {
    private static final long serialVersionUID = -10737855154725929L;
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 题材名称 A-B，比如汇率-下降
     * 第一次更新
     */
    private String subName;
    /**
     * 最高板
     * 需要更新的
     */
    private Integer combo;
    /**
     * 题材线名称，比如开开实业-主升
     * 第一次更新
     */
    private String subLineName;
    /**
     * 线状态：打高度、震荡、板块卡位、调整中
     * 第一次更新
     */
    private String lineState;
    /**
     * 开始持续时间
     * 第一次更新
     */
    private Date durationStart;
    /**
     * 结束持续时间
     * 需要更新的
     */
    private Date durationEnd;
    /**
     * 状态，有效1，无效0
     * 第一次更新
     */
    private String state;


}

