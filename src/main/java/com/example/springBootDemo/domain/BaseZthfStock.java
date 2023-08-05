package com.example.springBootDemo.domain;

import com.baomidou.mybatisplus.annotations.TableName;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.Date;
import java.io.Serializable;

/**
 * (BaseZthfStock)实体类
 *
 * @author makejava
 * @since 2023-08-05 23:59:59
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "")
@TableName("base_zthf_stock")
public class BaseZthfStock implements Serializable {
    private static final long serialVersionUID = -43872664651186314L;
    /**
     * 主键
     */
    private Integer id;
    /**
     * 股票编码
     */
    private String stockCode;
    /**
     * 股票名称
     */
    private String stockName;
    /**
     * 所属板块
     */
    private String plate;
    /**
     * 主业
     */
    private String mainBusiness;
    /**
     * 分支
     */
    private String nicheBusiness;
    /**
     * 流通市值,亿元
     */
    private Integer circulation;
    /**
     * 说明
     */
    private String instructions;
    /**
     * 连板数
     */
    private Integer combo;
    /**
     * 涨停时间
     */
    private LocalTime hardenTime;
    /**
     * 最终涨停时间
     */
    private LocalTime finalHardenTime;
    /**
     * 涨停类型
     */
    private String hardenType;
    /**
     * 涨停原因
     */
    private String reason;
    /**
     * 涨幅
     */
    private Double gains;
    /**
     * 昨日涨幅
     */
    private Double yesterdayGains;
    /**
     * 振幅
     */
    private Double amplitude;
    /**
     * 昨日振幅
     */
    private Double yesterdayAmplitude;
    /**
     * 换手
     */
    private Double changingHands;
    /**
     * 昨日换手
     */
    private Double yesterdayChangingHands;
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


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStockCode() {
        return stockCode;
    }

    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public String getMainBusiness() {
        return mainBusiness;
    }

    public void setMainBusiness(String mainBusiness) {
        this.mainBusiness = mainBusiness;
    }

    public String getNicheBusiness() {
        return nicheBusiness;
    }

    public void setNicheBusiness(String nicheBusiness) {
        this.nicheBusiness = nicheBusiness;
    }

    public Integer getCirculation() {
        return circulation;
    }

    public void setCirculation(Integer circulation) {
        this.circulation = circulation;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public Integer getCombo() {
        return combo;
    }

    public void setCombo(Integer combo) {
        this.combo = combo;
    }

    public LocalTime getHardenTime() {
        return hardenTime;
    }

    public void setHardenTime(LocalTime hardenTime) {
        this.hardenTime = hardenTime;
    }

    public LocalTime getFinalHardenTime() {
        return finalHardenTime;
    }

    public void setFinalHardenTime(LocalTime finalHardenTime) {
        this.finalHardenTime = finalHardenTime;
    }

    public String getHardenType() {
        return hardenType;
    }

    public void setHardenType(String hardenType) {
        this.hardenType = hardenType;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Double getGains() {
        return gains;
    }

    public void setGains(Double gains) {
        this.gains = gains;
    }

    public Double getYesterdayGains() {
        return yesterdayGains;
    }

    public void setYesterdayGains(Double yesterdayGains) {
        this.yesterdayGains = yesterdayGains;
    }

    public Double getAmplitude() {
        return amplitude;
    }

    public void setAmplitude(Double amplitude) {
        this.amplitude = amplitude;
    }

    public Double getYesterdayAmplitude() {
        return yesterdayAmplitude;
    }

    public void setYesterdayAmplitude(Double yesterdayAmplitude) {
        this.yesterdayAmplitude = yesterdayAmplitude;
    }

    public Double getChangingHands() {
        return changingHands;
    }

    public void setChangingHands(Double changingHands) {
        this.changingHands = changingHands;
    }

    public Double getYesterdayChangingHands() {
        return yesterdayChangingHands;
    }

    public void setYesterdayChangingHands(Double yesterdayChangingHands) {
        this.yesterdayChangingHands = yesterdayChangingHands;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getModifedDate() {
        return modifedDate;
    }

    public void setModifedDate(Date modifedDate) {
        this.modifedDate = modifedDate;
    }

    public String getModifedBy() {
        return modifedBy;
    }

    public void setModifedBy(String modifedBy) {
        this.modifedBy = modifedBy;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

}

