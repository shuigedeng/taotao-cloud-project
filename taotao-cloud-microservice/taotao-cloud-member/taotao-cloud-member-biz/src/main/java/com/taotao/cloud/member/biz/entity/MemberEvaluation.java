package com.taotao.cloud.member.biz.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 会员商品评价
 *
 * @since 2020-02-25 14:10:16
 */
@Entity
@Table(name = MemberEvaluation.TABLE_NAME)
@TableName(MemberEvaluation.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = MemberEvaluation.TABLE_NAME, comment = "会员商品评价表")
public class MemberEvaluation extends BaseSuperEntity<MemberEvaluation, Long> {

	public static final String TABLE_NAME = "li_member_evaluation";

	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(32) not null comment '会员ID'")
	private String memberId;


	@Column(name = "store_id", nullable = false, columnDefinition = "varchar(32) not null comment '店铺ID'")
	private String storeId;


	@Column(name = "store_name", nullable = false, columnDefinition = "varchar(32) not null comment '店铺名称'")
	private String storeName;


	@Column(name = "goods_id", nullable = false, columnDefinition = "varchar(32) not null comment '商品ID'")
	private String goodsId;


	@Column(name = "sku_id", nullable = false, columnDefinition = "varchar(32) not null comment 'SKU ID'")
	private String skuId;


	@Column(name = "member_name", nullable = false, columnDefinition = "varchar(32) not null comment '会员名称'")
	private String memberName;


	@Column(name = "member_profile", nullable = false, columnDefinition = "varchar(32) not null comment '会员头像'")
	private String memberProfile;


	@Column(name = "goods_name", nullable = false, columnDefinition = "varchar(32) not null comment '商品名称'")
	private String goodsName;

	@Column(name = "goods_image", nullable = false, columnDefinition = "varchar(32) not null comment '商品图片'")
	private String goodsImage;


	@Column(name = "order_no", nullable = false, columnDefinition = "varchar(32) not null comment '订单号'")
	private String orderNo;

	@Column(name = "grade", nullable = false, columnDefinition = "varchar(32) not null comment '好中差评 , GOOD：好评，MODERATE：中评，WORSE：差评'")
	private String grade;

	@Column(name = "content", nullable = false, columnDefinition = "varchar(32) not null comment '评价内容'")
	private String content;

	@Column(name = "images", nullable = false, columnDefinition = "varchar(32) not null comment '评价图片'")
	private String images;


	@Column(name = "status", nullable = false, columnDefinition = "varchar(32) not null comment '状态  OPEN 正常 ,CLOSE 关闭'")
	private String status;

	@Column(name = "reply", nullable = false, columnDefinition = "varchar(32) not null comment '评价回复'")
	private String reply;

	@Column(name = "reply_image", nullable = false, columnDefinition = "varchar(32) not null comment '评价回复图片'")
	private String replyImage;

	@Column(name = "have_image", nullable = false, columnDefinition = "varchar(32) not null comment '评论是否有图片 true 有 ,false 没有'")
	private Boolean haveImage;

	@Column(name = "have_reply_image", nullable = false, columnDefinition = "varchar(32) not null comment '回复是否有图片 true 有 ,false 没有'")
	private Boolean haveReplyImage;

	@Column(name = "reply_status", nullable = false, columnDefinition = "boolean not null default false comment '回复状态'")
	private Boolean replyStatus;

	@Column(name = "delivery_score", nullable = false, columnDefinition = "int not null default 0 comment '物流评分'")
	private Integer deliveryScore;

	@Column(name = "service_score", nullable = false, columnDefinition = "int not null default 0 comment '服务评分'")
	private Integer serviceScore;

	@Column(name = "description_score", nullable = false, columnDefinition = "int not null default 0 comment '描述评分'")
	private Integer descriptionScore;

	//public MemberEvaluation(MemberEvaluationDTO memberEvaluationDTO, GoodsSku goodsSku, Member member,Order order){
	//    //复制评价信息
	//    BeanUtils.copyProperties(memberEvaluationDTO, this);
	//    //设置会员
	//    this.memberId=member.getId();
	//    //会员名称
	//    this.memberName=member.getNickName();
	//    //设置会员头像
	//    this.memberProfile=member.getFace();
	//    //商品名称
	//    this.goodsName=goodsSku.getGoodsName();
	//    //商品图片
	//    this.goodsImage=goodsSku.getThumbnail();
	//    //设置店铺ID
	//    this.storeId=order.getStoreId();
	//    //设置店铺名称
	//    this.storeName=order.getStoreName();
	//    //设置订单编号
	//    this.orderNo=order.getSn();
	//    //是否包含图片
	//    this.haveImage=StringUtils.isNotEmpty(memberEvaluationDTO.getImages());
	//    //默认开启评价
	//    this.status=SwitchEnum.OPEN.name();
	//}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}

	public String getSkuId() {
		return skuId;
	}

	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public String getMemberProfile() {
		return memberProfile;
	}

	public void setMemberProfile(String memberProfile) {
		this.memberProfile = memberProfile;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getGoodsImage() {
		return goodsImage;
	}

	public void setGoodsImage(String goodsImage) {
		this.goodsImage = goodsImage;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getImages() {
		return images;
	}

	public void setImages(String images) {
		this.images = images;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getReply() {
		return reply;
	}

	public void setReply(String reply) {
		this.reply = reply;
	}

	public String getReplyImage() {
		return replyImage;
	}

	public void setReplyImage(String replyImage) {
		this.replyImage = replyImage;
	}

	public Boolean getHaveImage() {
		return haveImage;
	}

	public void setHaveImage(Boolean haveImage) {
		this.haveImage = haveImage;
	}

	public Boolean getHaveReplyImage() {
		return haveReplyImage;
	}

	public void setHaveReplyImage(Boolean haveReplyImage) {
		this.haveReplyImage = haveReplyImage;
	}

	public Boolean getReplyStatus() {
		return replyStatus;
	}

	public void setReplyStatus(Boolean replyStatus) {
		this.replyStatus = replyStatus;
	}

	public Integer getDeliveryScore() {
		return deliveryScore;
	}

	public void setDeliveryScore(Integer deliveryScore) {
		this.deliveryScore = deliveryScore;
	}

	public Integer getServiceScore() {
		return serviceScore;
	}

	public void setServiceScore(Integer serviceScore) {
		this.serviceScore = serviceScore;
	}

	public Integer getDescriptionScore() {
		return descriptionScore;
	}

	public void setDescriptionScore(Integer descriptionScore) {
		this.descriptionScore = descriptionScore;
	}
}
