package com.example.springBootDemo.entity;


import java.util.Date;

/**
 * @所属模块<p>
 * @描述<p>
 * @创建人 xiaoYe
 * @创建时间 2023-9-12 17:01
 * @Copyright (c) 2020 inc. all rights reserved<p>
 * @公司名称
 */
public class Goods {


    //为了生成 1 2 3 ...序列号
    private Integer order;//序号
    //商品所属类别展现的文字
    private String typeName;
    //格式化的日期
    private String dateStr;

    //商品编号，主键（Integer类型的取值）
    private Integer no;
    //商品名称（String类型的取值）
    private String name;
    //1 食品 2 服装 3 酒水 4 花卉
    //商品所属类别（Integer类型的取值，对应的数值要转成相应的文字）
    private Integer type;
    //商品保质器（测试日期值得获取）
    private Date shelfLife;
    //库存是否还有?0 无 1有(测试Integer类型的三目运算)
    private Integer isHave;

    //该商品是否经过了审核"0" 未过，"1" 通过(测试String类型的三目运算)
    private String isAudit;

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Integer getNo() {
        return no;
    }

    public void setNo(Integer no) {
        this.no = no;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Date getShelfLife() {
        return shelfLife;
    }

    public void setShelfLife(Date shelfLife) {
        this.shelfLife = shelfLife;
    }

    public Integer getIsHave() {
        return isHave;
    }

    public void setIsHave(Integer isHave) {
        this.isHave = isHave;
    }

    public String getIsAudit() {
        return isAudit;
    }

    public void setIsAudit(String isAudit) {
        this.isAudit = isAudit;
    }

    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    public Goods(Integer no, String name, Integer type, Date shelfLife, Integer isHave, String isAudit) {
        this.no = no;
        this.name = name;
        this.type = type;
        this.shelfLife = shelfLife;
        this.isHave = isHave;
        this.isAudit = isAudit;
    }

    @Override
    public String toString() {
        return "Goods{" +
                "order=" + order +
                ", typeName='" + typeName + '\'' +
                ", dateStr='" + dateStr + '\'' +
                ", no=" + no +
                ", name='" + name + '\'' +
                ", isHave=" + isHave +
                ", isAudit='" + isAudit + '\'' +
                '}';
    }
}
