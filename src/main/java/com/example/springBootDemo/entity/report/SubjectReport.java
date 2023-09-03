package com.example.springBootDemo.entity.report;

import com.alibaba.excel.util.DateUtils;
import com.example.springBootDemo.util.excel.Excel;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * 涨停报表
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class SubjectReport implements Serializable {

    @Excel(name = "题材")
    private String subName;

    @Excel(name = "题材说明")
    private String subInstructions;

    @Excel(name = "逻辑线")
    private String subLineName;

    @Excel(name = "高度")
    private String combo;

    @Excel(name = "开始日期")
    private String durationStart;

    @Excel(name = "结束日期")
    private String durationEnd;

    @Excel(name = "行业")
    private String mainBusiness;

    @Excel(name = "核心标的")
    private String coreName;

    @Excel(name = "辨识度")
    private String helpName;

    @Excel(name = "涨停数")
    private Integer countZt;

    @Excel(name = "回封数")
    private Integer countZthf;

//    @Excel(name = "模式")
    private String model;

    @Excel(name = "明细说明")
    private String instructions;

    @Excel(name = "日期",dateFormat= DateUtils.DATE_FORMAT_10)
    private Date createDate;

    @Excel(name = "星期")
    private String week;

    private static final long serialVersionUID = 1L;
}