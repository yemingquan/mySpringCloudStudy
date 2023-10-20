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


/**
 * 模式——其他(ConfModelOther)实体类
 *
 * @author makejava
 * @since 2023-10-06 22:15:46
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "模式-其他")
@TableName("conf_model_other")
public class ConfModelOther implements Serializable {
    private static final long serialVersionUID = 939091764137901787L;
    /**
     * 配置
     */
    @TableId(type = IdType.INPUT)
    @Excel(name = "配置")
    private String conf;
    /**
     * 主题
     */
    @Excel(name = "主题")
    private String topic;
    /**
     * 模式名称
     */
    @Excel(name = "模式环境")
    private String confModelName;
    /**
     * 类型 tomorrow/today
     */
    @Excel(name = "预期类型")
    private String type;
    /**
     * 名称
     */
    @Excel(name = "模式名称",width = 30)
    private String name;
    /**
     * 特征
     */
    @Excel(name = "特征",width = 55)
    private String attr;
    /**
     * 预期
     */
    private String behaviour;
    /**
     * 说明
     */
    @Excel(name = "预期",width = 55)
    private String instructions;
    /**
     * 备注
     */
    @Excel(name = "备注",width = 55)
    private String remark;
    /**
     * 状态，有效1，无效0
     */
    @Excel(name = "状态")
    private String state;

}

