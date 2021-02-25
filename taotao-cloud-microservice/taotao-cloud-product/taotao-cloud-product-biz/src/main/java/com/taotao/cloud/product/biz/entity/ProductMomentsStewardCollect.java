package com.taotao.cloud.product.biz.entity;


import com.taotao.cloud.data.jpa.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import javax.persistence.Table;
import java.time.LocalDateTime;

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
@Table(name = "tt_product_moments_steward_collect")
@org.hibernate.annotations.Table(appliesTo = "tt_product_moments_steward_collect", comment = "商品信息扩展表")
public class ProductMomentsStewardCollect extends BaseEntity {

    private Long stewardId;

    private Long momentsId;

    private LocalDateTime collectTime;

}
