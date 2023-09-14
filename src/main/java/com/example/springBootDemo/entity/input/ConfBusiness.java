package com.example.springBootDemo.entity.input;

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
 * 行业配置化(ConfBusiness)实体类
 *
 * @author makejava
 * @since 2023-09-13 16:37:28
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "行业配置化")
@TableName("conf_business")
public class ConfBusiness implements Serializable {
    private static final long serialVersionUID = -40100652532530046L;
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 行业名称
     */
    @Excel(name = "名称")
    private String busName;
    /**
     * 属性/影响 ;分隔
     */
    @Excel(name = "属性")
    private String attr;
    /**
     * 板块说明，板块的状态，市场的对这个行业的想法，板块的风险和机会
     */
    @Excel(name = "说明", width = 100)
    private String instructions;
    /**
     * 行业/题材
     */
    @Excel(name = "类型")
    private String type;
    /**
     * 核心标的集合，逗号分割
     */
    @Excel(name = "辨识度标的", width = 25)
    private String coreList;
    /**
     * 标的集合，逗号分割
     */
    @Excel(name = "标的", width = 60)
    private String list;
    /**
     * 核心标的code集合，逗号分割
     */
    private String codeCoreList;
    /**
     * 标的code集合，逗号分割
     */
    private String codeList;
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
     * 修改时间
     */
    private Date modifedDate;
    /**
     * 修改者
     */
    private String modifedBy;


}

