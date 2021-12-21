package com.taotao.cloud.member.biz.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 会员积分历史
 *
 * @since 2020-02-25 14:10:16
 */
@Entity
@Table(name = MemberPointsHistory.TABLE_NAME)
@TableName(MemberPointsHistory.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = MemberPointsHistory.TABLE_NAME, comment = "会员积分历史表")
public class MemberPointsHistory extends BaseSuperEntity<MemberPointsHistory, Long> {

	public static final String TABLE_NAME = "li_member_points_history";

	@Column(name = "create_by", nullable = false, columnDefinition = "varchar(32) not null comment '创建者'")
	private String createBy;

	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(32) not null comment '会员ID'")
	private String memberId;

	@Column(name = "member_name", nullable = false, columnDefinition = "varchar(32) not null comment '会员名称'")
	private String memberName;

	@Column(name = "point", nullable = false, columnDefinition = "bigint not null default 0 comment '当前积分'")
	private Long point;

	@Column(name = "before_point", nullable = false, columnDefinition = "bigint not null default 0 comment '消费之前积分'")
	private Long beforePoint;

	@Column(name = "variable_point", nullable = false, columnDefinition = "bigint not null default 0 comment '变动积分'")
	private Long variablePoint;

	@Column(name = "content", nullable = false, columnDefinition = "varchar(32) not null comment 'content'")
	private String content;

	/**
	 * @see cn.lili.modules.member.entity.enums.PointTypeEnum
	 */
	@Column(name = "point_type", nullable = false, columnDefinition = "varchar(32) not null comment '积分类型'")
	private String pointType;

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

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
