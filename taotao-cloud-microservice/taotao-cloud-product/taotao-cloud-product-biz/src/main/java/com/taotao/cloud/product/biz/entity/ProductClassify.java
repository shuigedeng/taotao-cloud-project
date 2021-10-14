package com.taotao.cloud.product.biz.entity;


import com.taotao.cloud.data.jpa.entity.JpaSuperEntity;

import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * 商品分类表
 * @author shuigedeng
 */
//@Entity
@Table(name = "tt_product_classify")
@org.hibernate.annotations.Table(appliesTo = "tt_product_classify", comment = "商品分类表")
public class ProductClassify extends JpaSuperEntity {

    private static final long serialVersionUID = 4623225062695180820L;

    private String name;

    private short sequence;

    private String path;

    private LocalDateTime updateTime;

    private Integer status;

    private Integer oneLevelCommission;

    private Integer twoLevelCommission;

    private Long companyId;
}
