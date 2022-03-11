package com.taotao.cloud.member.biz.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.member.api.enums.PointTypeEnum;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 会员积分历史表
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-03-11 15:26:14
 */
@Entity
@Table(name = MemberPointsHistory.TABLE_NAME)
@TableName(MemberPointsHistory.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = MemberPointsHistory.TABLE_NAME, comment = "会员积分历史表")
public class MemberPointsHistory extends BaseSuperEntity<MemberPointsHistory, Long> {

	public static final String TABLE_NAME = "tt_member_points_history";

	/**
	 * 会员id
	 */
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(65) not null comment '会员ID'")
	private String memberId;

	/**
	 * 会员名称
	 */
	@Column(name = "member_name", nullable = false, columnDefinition = "varchar(64) not null comment '会员名称'")
	private String memberName;

	/**
	 * 当前积分
	 */
	@Column(name = "point", nullable = false, columnDefinition = "bigint not null default 0 comment '当前积分'")
	private Long point;

	/**
	 * 消费之前积分
	 */
	@Column(name = "before_point", nullable = false, columnDefinition = "bigint not null default 0 comment '消费之前积分'")
	private Long beforePoint;

	/**
	 * 变动积分
	 */
	@Column(name = "variable_point", nullable = false, columnDefinition = "bigint not null default 0 comment '变动积分'")
	private Long variablePoint;

	/**
	 * 内容
	 */
	@Column(name = "content", nullable = false, columnDefinition = "varchar(32) not null comment '内容'")
	private String content;

	/**
	 * 积分类型
	 *
	 * @see PointTypeEnum
	 */
	@Column(name = "point_type", nullable = false, columnDefinition = "varchar(32) not null comment '积分类型'")
	private String pointType;

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

	public Long getPoint() {
		return point;
	}

	public void setPoint(Long point) {
		this.point = point;
	}

	public Long getBeforePoint() {
		return beforePoint;
	}

	public void setBeforePoint(Long beforePoint) {
		this.beforePoint = beforePoint;
	}

	public Long getVariablePoint() {
		return variablePoint;
	}

	public void setVariablePoint(Long variablePoint) {
		this.variablePoint = variablePoint;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getPointType() {
		return pointType;
	}

	public void setPointType(String pointType) {
		this.pointType = pointType;
	}
}
