package com.taotao.cloud.product.biz.entity;


import com.taotao.cloud.data.jpa.entity.BaseEntity;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * 商品分类表
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
@Table(name = "tt_product_classify")
@org.hibernate.annotations.Table(appliesTo = "tt_product_classify", comment = "商品分类表")
public class ProductClassify extends BaseEntity {

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
