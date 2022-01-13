package com.taotao.cloud.sys.biz.springboot.domain;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 基金公司(FundComany)实体类
 *
 * @author makejava
 * @since 2020-03-26 19:26:23
 */
@Entity
public class FundComany implements Serializable {
    private static final long serialVersionUID = -77419884194040864L;
    /**
    * 基金公司ID
    */
    @Id
    private Integer fundCompanyId;
    /**
    * 基金公司全称
    */
    @Column
    private String fundCompanyFullName;
    /**
    * 基金创立时间
    */
    @Column
    private String establishDate;
    /**
    * 基金数量
    */
    @Column
    private Integer fundsCount;
    /**
    * 基金公司管理者
    */
    @Column
    private String fundCompanyManager;
    /**
    * 基金公司全称（拼音首字母）
    */
    @Column
    private String fundCompanyPyName;
    @Column
    private String field1;
    /**
    * 基金规模
    */
    @Column
    private Double fundsAmount;
    /**
    * 星级
    */
    @Column
    private String rankingStar;
    /**
    * 基金公司简称
    */
    @Column
    private String fundCompanySimpleName;
    @Column
    private String field2;
    /**
    * 更新日期
    */
    @Column
    private String cmpTime;


    public Integer getFundCompanyId() {
        return fundCompanyId;
    }

    public void setFundCompanyId(Integer fundCompanyId) {
        this.fundCompanyId = fundCompanyId;
    }

    public String getFundCompanyFullName() {
        return fundCompanyFullName;
    }

    public void setFundCompanyFullName(String fundCompanyFullName) {
        this.fundCompanyFullName = fundCompanyFullName;
    }

    public String getEstablishDate() {
        return establishDate;
    }

    public void setEstablishDate(String establishDate) {
        this.establishDate = establishDate;
    }

    public Integer getFundsCount() {
        return fundsCount;
    }

    public void setFundsCount(Integer fundsCount) {
        this.fundsCount = fundsCount;
    }

    public String getFundCompanyManager() {
        return fundCompanyManager;
    }

    public void setFundCompanyManager(String fundCompanyManager) {
        this.fundCompanyManager = fundCompanyManager;
    }

    public String getFundCompanyPyName() {
        return fundCompanyPyName;
    }

    public void setFundCompanyPyName(String fundCompanyPyName) {
        this.fundCompanyPyName = fundCompanyPyName;
    }

    public String getField1() {
        return field1;
    }

    public void setField1(String field1) {
        this.field1 = field1;
    }

    public Double getFundsAmount() {
        return fundsAmount;
    }

    public void setFundsAmount(Double fundsAmount) {
        this.fundsAmount = fundsAmount;
    }

    public String getRankingStar() {
        return rankingStar;
    }

    public void setRankingStar(String rankingStar) {
        this.rankingStar = rankingStar;
    }

    public String getFundCompanySimpleName() {
        return fundCompanySimpleName;
    }

    public void setFundCompanySimpleName(String fundCompanySimpleName) {
        this.fundCompanySimpleName = fundCompanySimpleName;
    }

    public String getField2() {
        return field2;
    }

    public void setField2(String field2) {
        this.field2 = field2;
    }

    public String getCmpTime() {
        return cmpTime;
    }

    public void setCmpTime(String cmpTime) {
        this.cmpTime = cmpTime;
    }

}
