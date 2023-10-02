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
 * 日期(BaseDate)实体类
 *
 * @author makejava
 * @since 2023-09-03 00:27:42
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "日期")
@TableName("base_date")
public class BaseDate implements Serializable {
    private static final long serialVersionUID = -70403995721587044L;
    /**
     * 日期
     */
    @TableId(type = IdType.INPUT)
    private Date date;
    /**
     * 阴历日期
     */
    private Date lunar;
    /**
     * 名称
     */
    private String name;
    /**
     * 星期几
     */
    private String week;
    /**
     * 0工作日、1周末、2节日、3调休补班
     */
    private String type;
    /**
     * 修改标记，0未修改，1已修改
     */
    private String updateFlag;
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
     * 备注说明
     */
    private String remark;


}

