package com.example.springBootDemo.entity.game;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;


/**
 * 缩写
 *
 * @author makejava
 * @since 2023-08-28 20:05:15
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShortNameDto implements Serializable {
    private static final long serialVersionUID = 139155789162003218L;

    @Excel(name = "缩写")
    private String shortName;

    private String stockName;

    private String stockCode;

    @Excel(name = "数量")
    private Integer count;

    @Excel(name = "名称",width = 200)
    private List<String> infoList;
}

