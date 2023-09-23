package com.example.springBootDemo.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotations.TableField;
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
 * 市场(BaseMarket)实体类
 *
 * @author makejava
 * @since 2023-09-22 17:51:36
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "市场概况")
@TableName("base_market")
public class BaseMarket implements Serializable {
    private static final long serialVersionUID = 356412307508911502L;

    @Excel(name = "日期")
    @TableId(type = IdType.INPUT)
    private Date date;

    @Excel(name = "星期")
    @TableField(exist = false)
    private String week;

    @Excel(name = "关键词")
    private String keyWords;

    @Excel(name = "市场动态")
    private String marketTrends;
    /**
     * 市场主题/情绪节点
     */
    @Excel(name = "市场主题")
    private String subject;
    /**
     * 量能
     */
    @Excel(name = "量能")
    private String vol;
    /**
     * 老师的思路
     */
    @Excel(name = "老师的思路")
    private String teacherThinking;
    /**
     * 说明（过去的数据则用来存：连板梯队）
     */
    @Excel(name = "连板梯队")
    private String instructions;
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
    /**
     * 备注
     */
    private String remark;


}

