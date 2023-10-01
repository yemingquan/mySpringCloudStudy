package com.example.springBootDemo.entity.dto;

import com.baomidou.mybatisplus.annotations.TableField;
import com.example.springBootDemo.entity.base.BaseReportStock;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @所属模块<p>
 * @描述<p>
 * @创建人 xiaoYe
 * @创建时间 2023-8-26 22:10
 * @Copyright (c) 2020 inc. all rights reserved<p>
 * @公司名称
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QueryStockDto extends BaseReportStock {

    @TableField(exist = false)
    private Date startDate;

    @TableField(exist = false)
    private Date endDate;

    @TableField(exist = false)
    private List<String> stockNameList;
}
