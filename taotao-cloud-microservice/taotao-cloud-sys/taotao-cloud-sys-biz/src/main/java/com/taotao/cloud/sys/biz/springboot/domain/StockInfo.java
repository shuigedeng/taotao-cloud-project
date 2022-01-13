package com.taotao.cloud.sys.biz.springboot.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @author duhongming
 * @version 1.0
 * @description TODO
 * @date 2020/3/12 10:47
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class StockInfo {
    /**
     * 交易所+股票代码
     */
    @Id
    @Column(nullable = false)
    private String symbol;

    /**
     * 股票代码
     */
    @Column(nullable = false)
    private String code;

    /**
     * 股票名称
     */
    @Column(nullable = false)
    private String name;

    /**
     * 上市日期
     */
    @Column
    private String ipoDate;

    /**
     * 注册地址
     */
    @Column
    private String companyAddress;

    /**
     * 注册地址（省）
     */
    @Column
    private String companyProvince;

    @Override
    public String toString() {
        return symbol +
                "," + code +
                "," + name +
                "," + ipoDate +
                "," + companyAddress +
                "," + companyProvince;
    }
}
