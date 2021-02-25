package com.taotao.cloud.product.biz.entity;

import com.taotao.cloud.data.jpa.entity.BaseEntity;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * @author dengtao
 */
@Data
@SuperBuilder
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
//@Entity
@Table(name = "tt_product_spec_detail")
@org.hibernate.annotations.Table(appliesTo = "tt_product_spec_detail", comment = "商品信息扩展表")
public class ProductSpecDetail extends BaseEntity {

    private Product product;

    private String shelfNum;

    private String name;

    private String attributeJson;

    private int inventory;

    private BigDecimal offerPrice;

    private BigDecimal costPrice;

    private BigDecimal minSellPrice;

    private BigDecimal maxSellPrice;

    private String remark;

    private int sellCount;

    private String sourceId;
}
