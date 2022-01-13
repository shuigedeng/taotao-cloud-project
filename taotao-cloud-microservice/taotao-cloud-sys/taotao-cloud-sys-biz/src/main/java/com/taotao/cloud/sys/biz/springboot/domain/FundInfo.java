package com.taotao.cloud.sys.biz.springboot.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;

/**
 * @author duhongming
 * @version 1.0
 * @description TODO
 * @date 2019-11-15 20:50
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Accessors(chain = true)
public class FundInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 基金代码
     */
    @Column(nullable = false)
    private String fundCode;

    /**
     * 基金名称
     */
    @Column(nullable = false)
    private String fundName;

    /**
     * 每万份收益
     */
    @Column
    private Double everyThanAccrual;

    /**
     * 7日年化收益率（%）
     */
    @Column
    private Double annualizedYield7;

    /**
     * 单位净值
     */
    @Column
    private Double unitNetValue;

    /**
     * 累计净值
     */
    @Column
    private Double cumulativeNetValue;

    /**
     * 日增长率
     */
    @Column
    private Double dailyGrowthRate;

    /**
     * 净值日期
     */
    @Column(nullable = false)
    private String netvalueDate;
}
