package com.taotao.cloud.product.biz.entity;


import com.taotao.cloud.data.jpa.entity.BaseEntity;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商品信息扩展表
 *
 * @author dengtao
 * @date 2020/4/30 16:09
 */
@Data
@SuperBuilder
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
//@Entity
@Table(name = "tt_product_ext")
@org.hibernate.annotations.Table(appliesTo = "tt_product_ext", comment = "商品信息扩展表")
public class ProductExt extends BaseEntity {

    private int inventory;

    private BigDecimal minPrice;

    private BigDecimal maxPrice;

    private int collectionTimes;

    private int shareTimes;

    private int sellCount;

    private int sum;

    private int good;

    private int bad;

    private int hasImg;

    private int append;

    private int sensitiveWord;

    private int repeatCustomer;

    private LocalDateTime lastCommentDate;

    private String source;

    private String sourceId;


}
