package com.taotao.cloud.member.biz.model.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.member.api.enums.PointTypeEnum;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 会员积分历史表
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-03-11 15:26:14
 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = MemberPointsHistory.TABLE_NAME)
@TableName(MemberPointsHistory.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = MemberPointsHistory.TABLE_NAME, comment = "会员积分历史表")
public class MemberPointsHistory extends BaseSuperEntity<MemberPointsHistory, Long> {

	public static final String TABLE_NAME = "tt_member_points_history";

	/**
	 * 会员id
	 */
	@Column(name = "member_id", columnDefinition = "bigint not null comment '会员ID'")
	private Long memberId;

	/**
	 * 会员名称
	 */
	@Column(name = "member_name", columnDefinition = "varchar(255) not null comment '会员名称'")
	private String memberName;

	/**
	 * 当前积分
	 */
	@Column(name = "point", columnDefinition = "bigint not null default 0 comment '当前积分'")
	private Long point;

	/**
	 * 消费之前积分
	 */
	@Column(name = "before_point", columnDefinition = "bigint not null default 0 comment '消费之前积分'")
	private Long beforePoint;

	/**
	 * 变动积分
	 */
	@Column(name = "variable_point", columnDefinition = "bigint not null default 0 comment '变动积分'")
	private Long variablePoint;

	/**
	 * 内容
	 */
	@Column(name = "content", columnDefinition = "varchar(32) not null comment '内容'")
	private String content;

	/**
	 * 积分类型
	 *
	 * @see PointTypeEnum
	 */
	@Column(name = "point_type", columnDefinition = "varchar(32) not null comment '积分类型'")
	private String pointType;
}
