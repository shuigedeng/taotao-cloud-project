package com.taotao.cloud.goods.biz.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 商品评论表
 * <p>
 * todo 暂时未用 需要添加注释
 *
 * @author shuigedeng
 * @since 2020/4/30 16:06
 */
@Getter
@Setter
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = GoodsComment.TABLE_NAME)
@TableName(GoodsComment.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = GoodsComment.TABLE_NAME, comment = "商品评论表")
public class GoodsComment extends BaseSuperEntity<GoodsComment, Long> {

	public static final String TABLE_NAME = "tt_goods_comment";

	@Column(name = "goods_spec_ame", columnDefinition = "varchar(255) not null comment '会员ID'")
	private String goodsSpecName;

	@Column(name = "mall_id", columnDefinition = "bigint not null comment '会员ID'")
	private Long mallId;

	@Column(name = "scene_id", columnDefinition = "bigint not null comment '会员ID'")
	private Long sceneId;

	@Column(name = "customer_id", columnDefinition = "bigint not null comment '会员ID'")
	private Long customerId;

	@Column(name = "member_nick", columnDefinition = "varchar(255) not null comment '会员ID'")
	private String memberNick;

	@Column(name = "member_avatar", columnDefinition = "varchar(255) not null comment '会员ID'")
	private String memberAvatar;

	@Column(name = "order_code", columnDefinition = "varchar(255) not null comment '会员ID'")
	private String orderCode;

	@Column(name = "type", columnDefinition = "int not null comment '会员ID'")
	private Integer type;

	@Column(name = "rank", columnDefinition = "int not null comment '会员ID'")
	private Integer rank;

	@Column(name = "has_image", columnDefinition = "int not null comment '会员ID'")
	private Integer hasImage;

	@Column(name = "comment_pic_id", columnDefinition = "bigint not null comment '会员ID'")
	private Long commentPicId;

	@Column(name = "has_sen_word", columnDefinition = "int not null comment '会员ID'")
	private Integer hasSenWord;

	@Column(name = "origin_content", columnDefinition = "varchar(255) not null comment '会员ID'")
	private String originContent;

	@Column(name = "filter_content", columnDefinition = "varchar(255) not null comment '会员ID'")
	private String filterContent;

	@Column(name = "op_type", columnDefinition = "int not null comment '会员ID'")
	private Integer opType;

	@Column(name = "reply_status", columnDefinition = "int not null comment '会员ID'")
	private Integer replyStatus;

	@Column(name = "reply_content", columnDefinition = "varchar(255) not null comment '会员ID'")
	private String replyContent;

	@Column(name = "reply_ori_content", columnDefinition = "varchar(255) not null comment '会员ID'")
	private String replyOriContent;

	@Column(name = "reply_time", columnDefinition = "datetime not null comment '会员ID'")
	private LocalDateTime replyTime;

	@Column(name = "reply_user_id", columnDefinition = "bigint not null comment '会员ID'")
	private Long replyUserId;

	@Column(name = "reply_pic_id", columnDefinition = "bigint not null comment '会员ID'")
	private Long replyPicId;

	@Column(name = "has_add", columnDefinition = "int not null comment '会员ID'")
	private Integer hasAdd;

	@Column(name = "after_days", columnDefinition = "int not null comment '会员ID'")
	private Integer afterDays;

	@Column(name = "append_time", columnDefinition = "datetime not null comment '会员ID'")
	private LocalDateTime appendTime;

	@Column(name = "status", columnDefinition = "int not null comment '会员ID'")
	private Integer status;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
		GoodsComment that = (GoodsComment) o;
		return getId() != null && Objects.equals(getId(), that.getId());
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}
