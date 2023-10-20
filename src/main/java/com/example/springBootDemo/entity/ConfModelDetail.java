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


/**
 * 模式打法(ConfModelDetail)实体类
 *
 * @author makejava
 * @since 2023-10-06 19:07:41
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "模式打法")
@TableName("conf_model_detail")
public class ConfModelDetail implements Serializable {
    private static final long serialVersionUID = -18787795218164731L;
    /**
     * 适用类型，比如a1 、a2
     */
    private String modelType;
    /**
     * 明细类型，主键
     */
    @TableId(type = IdType.INPUT)
    private String modelDetailType;
    /**
     * 名字
     */
    private String name;
    /**
     * 特征
     */
    private String attr;
    /**
     * 行为
     */
    private String behaviour;
    /**
     * 说明，什么样的环境最适合，什么样的风险最高
     */
    private String instructions;
    /**
     * 状态，有效1，无效0
     */
    private String state;
    /**
     * 备注
     */
    private String remark;


}

