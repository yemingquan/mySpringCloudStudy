package com.example.springBootDemo.entity.report;

import com.baomidou.mybatisplus.annotations.TableField;
import com.example.springBootDemo.config.components.constant.DateConstant;
import com.example.springBootDemo.util.excel.Excel;
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
    private String id;
    /**
     * 距离兑现还相差多少天
     */
    @TableField(exist = false)
    private Integer range;

    @Excel(name = "名称")
    private String name;
    /**
     * 日期
     */
    @Excel(name = "日期",dateFormat= DateConstant.DATE_FORMAT_10)
    private Date date;

    @Excel(name = "星期")
    private String week;

    @Excel(name = "持续")
    private Integer duration;

    @Excel(name = "影响范围")
    private String scope;

    @Excel(name = "关键词")
    private String newsType;

    @Excel(name = "内容")
    private String content;

    //    @Excel(name = "相对盘中")
    private String happen;

//    @Excel(name = "类型")
    private String type;

    @Excel(name = "行业")
    private String mainBusiness;
    /**
     * 预期
     */
    @Excel(name = "标的")
    private String expect;

//    @Excel(name = "模式")
    private String expectMode;
    /**
     * 实际,如果真的引发市场波动，会更新这个字段
     */
    private String reality;
    /**
     * 优先级0-9，9最高
     */
//    @Excel(name = "优先级")
    private String priorityLevel;
    /**
     * 重要程度0-9，9最高
     */
//    @Excel(name = "重要")
    private Integer important;
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

