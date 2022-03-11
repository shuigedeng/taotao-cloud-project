package com.taotao.cloud.goods.biz.entity;


import com.taotao.cloud.data.jpa.entity.JpaSuperEntity;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Table;

/**
 * @author shuigedeng
 */
//@Entity
@Table(name = "tt_product_moments_steward_collect")
@org.hibernate.annotations.Table(appliesTo = "tt_product_moments_steward_collect", comment = "商品信息扩展表")
public class ProductMomentsStewardCollect extends JpaSuperEntity {
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private Long stewardId;
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private Long momentsId;
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private LocalDateTime collectTime;

}
