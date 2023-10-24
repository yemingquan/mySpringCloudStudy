package com.example.springBootDemo.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.example.springBootDemo.config.components.constant.DateConstant;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;


/**
 * 模式关系表(RelationConf)实体类
 *
 * @author makejava
 * @since 2023-10-07 09:11:30
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "模式关系表")
@TableName("relation_conf")
public class RelationConf implements Serializable {
    private static final long serialVersionUID = 121276134655285394L;
    /**
     * 主键
     */
    @TableId(type = IdType.NONE)
    @Excel(name = "主键")
    private Integer id;
    /**
     * 时间
     */
//    @JsonFormat(pattern = DateConstant.DATE_FORMAT_10)
    @Excel(name = "日期", format = DateConstant.DATE_FORMAT_10, width = 14)
    private Date date;
    /**
     * 模式 --会根据其他表而转码
     */
    @Excel(name = "模式", width = 18)
    private String model;
    /**
     * 股票名称
     */
    @Excel(name = "股票名称", width = 22)
    private String stockName;
    /**
     * 类型
     */
    @Excel(name = "类型", width = 10)
    private String type;
    /**
     * 模式其他 --会根据其他表而转码
     */
    @Excel(name = "模式其他", width = 50)
    private String modelOther;
    /**
     * 说明
     */
    @Excel(name = "说明", width = 110)
    private String instructions;


    /**
     * 如果这个字段填写了，会当做股票数据处理（区分于趋势数据）
     * 那么就会取出上一个上面字段，到对应的基础股票信息表去赋值，这个字段会被当做昨日说明来处理
     */
    @Excel(name = "今日说明", width = 110)
    @TableField(exist = false)
    private String todayInstructions;
}

