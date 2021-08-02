package com.taotao.cloud.product.biz.entity;


import com.taotao.cloud.data.jpa.entity.BaseEntity;
import groovy.transform.EqualsAndHashCode;
import groovy.transform.ToString;
import javax.persistence.Table;

/**
 * 商品销售范围表
 *
 * @author shuigedeng
 * @since 2020/4/30 16:04
 */
//@Entity
@Table(name = "tt_product_area")
@org.hibernate.annotations.Table(appliesTo = "tt_product_area", comment = "商品销售范围表")
public class ProductArea extends BaseEntity {

    private String regionJson;

    private int type;

}
