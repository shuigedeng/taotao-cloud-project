package com.taotao.cloud.goods.biz.model.entity;//package com.taotao.cloud.goods.biz.entity;
//
//import com.taotao.cloud.data.jpa.entity.JpaSuperEntity;
//import java.math.BigDecimal;
//import jakarta.persistence.Column;
//import jakarta.persistence.Table;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//import org.apache.commons.math3.stat.descriptive.summary.Product;
//
///**
// * @author shuigedeng
// */
//@Setter
//@Getter
//@Builder
//@AllArgsConstructor
//@NoArgsConstructor
////@Entity
//@Table(name = "tt_product_spec_detail")
//@org.hibernate.annotations.Table(appliesTo = "tt_product_spec_detail", comment = "商品信息扩展表")
//public class ProductSpecDetail extends JpaSuperEntity {
//	@Column(name = "member_id", columnDefinition = "varchar(255) not null comment '会员ID'")
//    private Product product;
//	@Column(name = "member_id", columnDefinition = "varchar(255) not null comment '会员ID'")
//    private String shelfNum;
//	@Column(name = "member_id", columnDefinition = "varchar(255) not null comment '会员ID'")
//    private String name;
//	@Column(name = "member_id", columnDefinition = "varchar(255) not null comment '会员ID'")
//    private String attributeJson;
//	@Column(name = "member_id", columnDefinition = "varchar(255) not null comment '会员ID'")
//    private int inventory;
//	@Column(name = "member_id", columnDefinition = "varchar(255) not null comment '会员ID'")
//    private BigDecimal offerPrice;
//	@Column(name = "member_id", columnDefinition = "varchar(255) not null comment '会员ID'")
//    private BigDecimal costPrice;
//	@Column(name = "member_id", columnDefinition = "varchar(255) not null comment '会员ID'")
//    private BigDecimal minSellPrice;
//	@Column(name = "member_id", columnDefinition = "varchar(255) not null comment '会员ID'")
//    private BigDecimal maxSellPrice;
//	@Column(name = "member_id", columnDefinition = "varchar(255) not null comment '会员ID'")
//    private String remark;
//	@Column(name = "member_id", columnDefinition = "varchar(255) not null comment '会员ID'")
//    private int sellCount;
//	@Column(name = "member_id", columnDefinition = "varchar(255) not null comment '会员ID'")
//    private String sourceId;
//}
