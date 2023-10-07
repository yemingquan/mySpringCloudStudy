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
 * 模式，同样一个环境可能会出现多种模式叠加，比如小盘、多概念、消息面、国资委控股(ConfModel)实体类
 *
 * @author makejava
 * @since 2023-10-06 15:52:23
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "模式")
@TableName("conf_model")
public class ConfModel implements Serializable {
    private static final long serialVersionUID = 403954534424349449L;
    /**
     * 适用类型，比如a1 、a2
     */
    @TableId(type = IdType.INPUT)
    @Excel(name = "类型")
    private String modelType;
    /**
     * 模式名称
     */
    @Excel(name = "名字")
    private String modelName;
    /**
     * 特征
     */
    @Excel(name = "特征")
    private String abbr;
    /**
     * 行为
     */
    @Excel(name = "行为")
    private String behaviour;
    /**
     * 说明，什么样的环境最适合，什么样的风险最高
     */
    @Excel(name = "说明")
    private String instructions;
    /**
     * 效果预测，比如1：救情绪、2：带节奏、3：超预期、4：产品 
     */
    private String chance;
    /**
     * 风险预测，比如1：跌停、2A杀 3断板 
     */
    private String risk;
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

