package com.example.springBootDemo.entity.input;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


/**
 * 题材线明细导入
 *
 * @author makejava
 * @since 2023-08-25 17:39:58
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaseSubjectDetail implements Serializable {
    private static final long serialVersionUID = -63101766358407601L;

    @Excel(name = "题材")
    private String subName;

    @Excel(name = "题材说明")
    private String subInstructions;

    @Excel(name = "逻辑线")
    private String subLineName;

    @Excel(name = "行业")
    private String mainBusiness;

    @Excel(name = "核心标的")
    private String coreName;

    @Excel(name = "助攻标的")
    private String helpName;

    @Excel(name = "预期")
    private String expect;

    @Excel(name = "模式")
    private String model;

    @Excel(name = "明细说明")
    private String instructions;

    @Excel(name = "日期")
    private String createDate;
}

