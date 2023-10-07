package com.example.springBootDemo.entity.input;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


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
public class Model implements Serializable {
    private static final long serialVersionUID = 403954534424349449L;

    @Excel(name = "类型")
    private String modelType;

    @Excel(name = "名字")
    private String modelName;

    @Excel(name = "特征")
    private String abbr;

    @Excel(name = "行为")
    private String behaviour;

    @Excel(name = "模式说明")
    private String instructions;
    /**
     * 效果预测，比如1：救情绪、2：带节奏、3：超预期、4：产品
     */
    private String chance;
    /**
     * 风险预测，比如1：跌停、2A杀 3断板
     */
    private String risk;

    @Excel(name = "明细类型")
    private String modelDetailType;

    @Excel(name = "明细名字")
    private String modelDetailName;
//    private String modelType;

    @Excel(name = "明细特点")
    private String detailAbbr;

    @Excel(name = "明细行为")
    private String detailBehaviour;

    @Excel(name = "明细说明")
    private String detailInstructions;

    /**
     * 状态，有效1，无效0
     */
    @Excel(name = "明细状态")
    private String detailState;
}

