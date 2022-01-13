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
 * @date 2019-12-08 11:08
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Accessors(chain = true)
public class AutohomeBrand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 车标
     */
    @Column(nullable = false)
    private String carBrandIconUrl;

    /**
     * 品牌
     */
    @Column(nullable = false)
    private String carBrand;
    @Column(nullable = false)
    private String carBrandUrl;

    /**
     * 车系
     */
    @Column(nullable = false)
    private String carSeries;
    @Column(nullable = false)
    private String carSeriesUrl;

    /**
     * 车型
     */
    @Column(nullable = false)
    private String carModel;
    @Column(nullable = false)
    private String carModelUrl;

    /**
     * 指导价
     */
    @Column(nullable = false)
    private String referencePrice;

    private String letterIndex;
}
