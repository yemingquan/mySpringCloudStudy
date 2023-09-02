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
 * 特殊日期(BaseDateSpecial)实体类
 *
 * @author makejava
 * @since 2023-09-03 00:28:06
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "特殊日期")
@TableName("base_date_special")
public class BaseDateSpecial implements Serializable {
    private static final long serialVersionUID = 969254780124548103L;
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 年份
     */
    private String year;
    /**
     * 起始日期，同一张表可以出现重复日期
     */
    private Date date;
    /**
     * 阴历日期
     */
    private Date lunar;
    /**
     * 星期几
     */
    private String week;
    /**
     * 持续时间，大于1天时填写，比如：1
     */
    private Integer duration;
    /**
     * 名称
     */
    private String name;
    /**
     * 来源：system代表公共，写特殊的代表谁去做
     */
    private String source;
    /**
     * 0未定义、1纪念日、2节日、3生日、4事件
     */
    private String type;
    /**
     * 优先级0-9，9最高
     */
    private String priorityLevel;
    /**
     * 重要程度0-9，9最高
     */
    private String important;
    /**
     * 说明,比如xxx今天生日，他最喜欢xxx
     */
    private String notice;
    /**
     * 通知类型，1邮件、2短信、3其他
     */
    private String noticeType;
    /**
     * 倒计时，距离系统还有几天
     */
    private Integer countDown;
    /**
     * 特殊倒计时，距离系统还有几个休息日，不包括该表数据
     */
    private Integer countDownSpecial;
    /**
     * 倒计时警告,剩余几天开始推送警告
     */
    private Integer countDownWarm;
    /**
     * 隐藏标记 0隐藏、1显示
     */
    private Integer showFlag;
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

