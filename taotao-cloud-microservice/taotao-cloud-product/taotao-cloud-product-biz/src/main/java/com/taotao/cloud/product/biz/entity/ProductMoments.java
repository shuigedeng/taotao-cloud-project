package com.taotao.cloud.product.biz.entity;


import com.taotao.cloud.data.jpa.entity.BaseEntity;
import groovy.transform.EqualsAndHashCode;
import groovy.transform.ToString;
import java.time.LocalDateTime;
import javax.persistence.Table;

//@Entity
@Table(name = "tt_product_moments")
@org.hibernate.annotations.Table(appliesTo = "tt_product_moments", comment = "商品信息扩展表")
public class ProductMoments extends BaseEntity {

    private Long productId;

    private String document;

    private Long picId;

    private Integer status;

    private Integer hasVideo;

    @Builder.Default
    private Long sendNum = 0L;

    @Builder.Default
    private Integer sort = 0;

    private LocalDateTime createDate;

    private LocalDateTime publishTime;

}
