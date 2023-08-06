package com.example.springBootDemo.entity;

import com.baomidou.mybatisplus.annotations.TableName;
import com.example.springBootDemo.util.excel.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;

import java.io.Serializable;
import java.util.Date;

/**
 * (BStudent)表实体类
 *
 * @author xiaoye
 * @since 2023-08-05 12:46:20
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "")
@TableName("b_student")
public class Student implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "${id}", required = false)
    private int id;

    @ApiModelProperty(value = "${name}", required = false)
    @Excel(name = "姓名", backgroundColor = IndexedColors.RED, suffix = "%",
            color = IndexedColors.BLUE, fontUnderLine = Font.U_SINGLE, prompt = "你好")
    private String name;

    @ApiModelProperty(value = "${createDate}", required = false)
    private Date createDate;


}