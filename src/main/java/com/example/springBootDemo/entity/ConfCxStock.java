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


/**
 * 次新股票(ConfCxStock)实体类
 *
 * @author makejava
 * @since 2023-08-19 14:39:12
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "次新股票")
@TableName("conf_cx_stock")
public class ConfCxStock implements Serializable {
    private static final long serialVersionUID = 772275700712697868L;
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 股票编码
     */
    @Excel(name = "代码", orderNum = "1")
    private String stockCode;
    /**
     * 股票名称
     */
    @Excel(name = "名称", orderNum = "2")
    private String stockName;


}

