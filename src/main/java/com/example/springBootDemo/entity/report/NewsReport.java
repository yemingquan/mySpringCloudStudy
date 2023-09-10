package com.example.springBootDemo.entity.report;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;


/**
 * 新闻(BaseDateNews)excel导入类
 *
 * @author makejava
 * @since 2023-09-03 00:27:57
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewsReport implements Serializable {
    private static final long serialVersionUID = 211816754774884331L;
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 日期
     */
    @Excel(name = "日期")
    private Date date;
    /**
     * 持续时间，大于1天时填写，比如：1
     */
    @Excel(name = "持续时间")
    private Integer duration;
    /**
     * 消息类型：0未定义、1大环境、2主线、3活跃板块、4其他 
     */
    @Excel(name = "消息类型")
    private String scope;
    /**
     * 标题
     */
    @Excel(name = "标题")
    private String newsType;
    /**
     * 内容
     */
    @Excel(name = "内容")
    private String content;
    /**
     * 发生时间：开盘前、盘中、收盘后
     */
    @Excel(name = "相对盘中")
    private String happen;
    /**
     * 1.延期消息(会议那种) 2.即时消息（一般盘中）
     */
    @Excel(name = "类型")
    private String type;
    /**
     * 影响行业，比如：一带一路/港口
     */
    @Excel(name = "行业")
    private String mainBusiness;
    /**
     * 预期
     */
    @Excel(name = "预期")
    private String expect;
    /**
     * 预期模式
     */
    private String expectMode;
    /**
     * 实际,如果真的引发市场波动，会更新这个字段
     */
    private String reality;
    /**
     * 优先级0-9，9最高
     */
    @Excel(name = "优先级")
    private String priorityLevel;
    /**
     * 重要程度0-9，9最高
     */
    @Excel(name = "重要")
    private String important;
    /**
     * 消息来源
     */
    private String source;
    /**
     * 说明
     */
    @Excel(name = "说明")
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
     * 备注说明
     */
    private String remark;


}

