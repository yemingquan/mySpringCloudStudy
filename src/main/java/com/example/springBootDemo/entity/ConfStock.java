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
 * 配置化自定义个股(ConfStock)实体类
 *
 * @author makejava
 * @since 2023-08-28 20:05:15
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "配置化自定义个股")
@TableName("conf_stock")
public class ConfStock implements Serializable {
    private static final long serialVersionUID = 139155789162003218L;
    /**
     * 股票编码
     */
    @Excel(name = "代码")
    @TableId(type = IdType.INPUT)
    private String stockCode;
    /**
     * 股票名称
     */
    @Excel(name = "名称")
    private String stockName;
    /**
     * 上市日期
     */
    private Date issueDate;
    /**
     * 发行价格
     */
    private Double price;
    /**
     * 所属板块
     */
    private String plate;
    /**
     * 主业
     */
    private String mainBusiness;
    /**
     * 分支
     */
    private String nicheBusiness;
    /**
     * 属性
     */
    private String attr;
    /**
     * 说明
     */
    private String instructions;
    /**
     * 我给的标签
     */
    private String tag;
    /**
     * 我的说明
     */
    private String myInstructions;
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

