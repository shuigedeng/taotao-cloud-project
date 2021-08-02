package com.taotao.cloud.product.biz.entity;


import com.taotao.cloud.data.jpa.entity.BaseEntity;
import groovy.transform.EqualsAndHashCode;
import groovy.transform.ToString;
import java.time.LocalDateTime;
import javax.persistence.Table;

/**
 * @author shuigedeng
 */
//@Entity
@Table(name = "tt_product_moments_steward_collect")
@org.hibernate.annotations.Table(appliesTo = "tt_product_moments_steward_collect", comment = "商品信息扩展表")
public class ProductMomentsStewardCollect extends BaseEntity {

    private Long stewardId;

    private Long momentsId;

    private LocalDateTime collectTime;

}
