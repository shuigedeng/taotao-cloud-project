package com.taotao.cloud.member.biz.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 会员等级表
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-03-11 15:16:55
 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = MemberGrade.TABLE_NAME)
@TableName(MemberGrade.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = MemberGrade.TABLE_NAME, comment = "会员等级表")
public class MemberGrade extends BaseSuperEntity<MemberGrade, Long> {

	public static final String TABLE_NAME = "tt_member_grade";

	/**
	 * 等级名称
	 */
	@Column(name = "grade_name", columnDefinition = "varchar(255) not null comment '等级名称'")
	private String gradeName;

	/**
	 * 等级图片
	 */
	@Column(name = "grade_image", columnDefinition = "varchar(1024) not null comment '等级图片'")
	private String gradeImage;

	/**
	 * 所需经验值
	 */
	@Column(name = "experience_value", columnDefinition = "int not null default 0 comment '所需经验值'")
	private Integer experienceValue;

	/**
	 * 是否为默认等级
	 */
	@Column(name = "defaulted", columnDefinition = "boolean not null default false comment '是否为默认等级'")
	private Boolean defaulted;
}
