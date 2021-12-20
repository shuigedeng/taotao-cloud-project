package com.taotao.cloud.store.biz.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.store.api.enums.StoreStatusEnum;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 店铺
 *
 * @since 2020-02-18 15:18:56
 */
@Entity
@Table(name = Store.TABLE_NAME)
@TableName(Store.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = Store.TABLE_NAME, comment = "店铺表")
public class Store extends BaseSuperEntity<Store, Long> {

	public static final String TABLE_NAME = "li_store";

	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员Id'")
	private String memberId;

	@Column(name = "member_name", nullable = false, columnDefinition = "varchar(64) not null comment '会员名称'")
	private String memberName;

	@Column(name = "store_name", nullable = false, columnDefinition = "varchar(64) not null comment '店铺名称'")
	private String storeName;

	@Column(name = "store_end_time", columnDefinition = "TIMESTAMP comment '店铺关闭时间'")
	private LocalDateTime storeEndTime;

	/**
	 * @see StoreStatusEnum
	 */
	@Column(name = "store_disable", nullable = false, columnDefinition = "varchar(64) not null comment '店铺状态'")
	private String storeDisable;

	@Column(name = "self_operated", nullable = false, columnDefinition = "boolean not null default true comment '是否自营'")
	private Boolean selfOperated;

	@Column(name = "store_logo", nullable = false, columnDefinition = "varchar(64) not null comment '店铺logo'")
	private String storeLogo;

	@Column(name = "store_center", nullable = false, columnDefinition = "varchar(64) not null comment '经纬度'")
	private String storeCenter;

	@Column(name = "store_desc", nullable = false, columnDefinition = "varchar(64) not null comment '店铺简介'")
	private String storeDesc;

	@Column(name = "store_address_path", nullable = false, columnDefinition = "varchar(64) not null comment '地址名称 逗号分割'")
	private String storeAddressPath;

	@Column(name = "store_address_id_path", nullable = false, columnDefinition = "varchar(64) not null comment '地址id 逗号分割 '")
	private String storeAddressIdPath;

	@Column(name = "store_address_detail", nullable = false, columnDefinition = "varchar(64) not null comment '详细地址'")
	private String storeAddressDetail;

	@Column(name = "description_score", nullable = false, columnDefinition = "decimal(10,2) not null default 0 comment '描述评分'")
	private BigDecimal descriptionScore;

	@Column(name = "service_score", nullable = false, columnDefinition = "decimal(10,2) not null default 0 comment '服务评分'")
	private BigDecimal serviceScore;

	@Column(name = "delivery_score", nullable = false, columnDefinition = "decimal(10,2) not null default 0 comment '交付分数'")
	private BigDecimal deliveryScore;

	@Column(name = "goods_num", nullable = false, columnDefinition = "int not null default 0 comment '商品数量'")
	private Integer goodsNum;

	@Column(name = "collection_num", nullable = false, columnDefinition = "int not null default 0 comment '收藏数量'")
	private Integer collectionNum;

	@Column(name = "yzf_sign", nullable = false, columnDefinition = "varchar(64) not null comment '腾讯云智服唯一标识'")
	private String yzfSign;

	@Column(name = "yzf_mp_sign", nullable = false, columnDefinition = "varchar(64) not null comment '腾讯云智服小程序唯一标识'")
	private String yzfMpSign;

	@Column(name = "merchant_euid", nullable = false, columnDefinition = "varchar(64) not null comment 'udesk IM标识'")
	private String merchantEuid;

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public LocalDateTime getStoreEndTime() {
		return storeEndTime;
	}

	public void setStoreEndTime(LocalDateTime storeEndTime) {
		this.storeEndTime = storeEndTime;
	}

	public String getStoreDisable() {
		return storeDisable;
	}

	public void setStoreDisable(String storeDisable) {
		this.storeDisable = storeDisable;
	}

	public Boolean getSelfOperated() {
		return selfOperated;
	}

	public void setSelfOperated(Boolean selfOperated) {
		this.selfOperated = selfOperated;
	}

	public String getStoreLogo() {
		return storeLogo;
	}

	public void setStoreLogo(String storeLogo) {
		this.storeLogo = storeLogo;
	}

	public String getStoreCenter() {
		return storeCenter;
	}

	public void setStoreCenter(String storeCenter) {
		this.storeCenter = storeCenter;
	}

	public String getStoreDesc() {
		return storeDesc;
	}

	public void setStoreDesc(String storeDesc) {
		this.storeDesc = storeDesc;
	}

	public String getStoreAddressPath() {
		return storeAddressPath;
	}

	public void setStoreAddressPath(String storeAddressPath) {
		this.storeAddressPath = storeAddressPath;
	}

	public String getStoreAddressIdPath() {
		return storeAddressIdPath;
	}

	public void setStoreAddressIdPath(String storeAddressIdPath) {
		this.storeAddressIdPath = storeAddressIdPath;
	}

	public String getStoreAddressDetail() {
		return storeAddressDetail;
	}

	public void setStoreAddressDetail(String storeAddressDetail) {
		this.storeAddressDetail = storeAddressDetail;
	}

	public BigDecimal getDescriptionScore() {
		return descriptionScore;
	}

	public void setDescriptionScore(BigDecimal descriptionScore) {
		this.descriptionScore = descriptionScore;
	}

	public BigDecimal getServiceScore() {
		return serviceScore;
	}

	public void setServiceScore(BigDecimal serviceScore) {
		this.serviceScore = serviceScore;
	}

	public BigDecimal getDeliveryScore() {
		return deliveryScore;
	}

	public void setDeliveryScore(BigDecimal deliveryScore) {
		this.deliveryScore = deliveryScore;
	}

	public Integer getGoodsNum() {
		return goodsNum;
	}

	public void setGoodsNum(Integer goodsNum) {
		this.goodsNum = goodsNum;
	}

	public Integer getCollectionNum() {
		return collectionNum;
	}

	public void setCollectionNum(Integer collectionNum) {
		this.collectionNum = collectionNum;
	}

	public String getYzfSign() {
		return yzfSign;
	}

	public void setYzfSign(String yzfSign) {
		this.yzfSign = yzfSign;
	}

	public String getYzfMpSign() {
		return yzfMpSign;
	}

	public void setYzfMpSign(String yzfMpSign) {
		this.yzfMpSign = yzfMpSign;
	}

	public String getMerchantEuid() {
		return merchantEuid;
	}

	public void setMerchantEuid(String merchantEuid) {
		this.merchantEuid = merchantEuid;
	}

	//public Store(Member member) {
	//    this.memberId = member.getId();
	//    this.memberName = member.getUsername();
	//    storeDisable = StoreStatusEnum.APPLY.value();
	//    selfOperated = false;
	//    deliveryScore = 5.0;
	//    serviceScore = 5.0;
	//    descriptionScore = 5.0;
	//    goodsNum = 0;
	//    collectionNum = 0;
	//}
	//
	//public Store(Member member, AdminStoreApplyDTO adminStoreApplyDTO) {
	//    BeanUtil.copyProperties(adminStoreApplyDTO, this);
	//
	//    this.memberId = member.getId();
	//    this.memberName = member.getUsername();
	//    storeDisable = StoreStatusEnum.APPLYING.value();
	//    selfOperated = false;
	//    deliveryScore = 5.0;
	//    serviceScore = 5.0;
	//    descriptionScore = 5.0;
	//    goodsNum = 0;
	//    collectionNum = 0;
}



}
