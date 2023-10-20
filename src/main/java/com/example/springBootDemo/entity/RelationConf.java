package com.example.springBootDemo.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
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
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 时间
     */
//    @JsonFormat(pattern = DateConstant.DATE_FORMAT_10)
    @Excel(name = "日期", format = DateConstant.DATE_FORMAT_10, width = 14)
    private Date date;
    /**
     * 模式
     */
    @Excel(name = "模式")
    private String model;
    /**
     * 股票名称
     */
    @Excel(name = "股票名称", width = 10)
    private String stockName;
    /**
     * 类型
     */
    @Excel(name = "类型", width = 10)
    private String type;
    /**
     * 模式其他
     */
    @Excel(name = "模式其他", width = 50)
    private String modelOther;
    /**
     * 说明
     */
    @Excel(name = "说明", width = 110)
    private String instructions;


}

