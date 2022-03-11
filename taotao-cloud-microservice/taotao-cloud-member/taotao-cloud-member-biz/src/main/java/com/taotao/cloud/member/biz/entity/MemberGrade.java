package com.taotao.cloud.member.biz.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 会员等级表
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-03-11 15:16:55
 */
@Entity
@Table(name = MemberGrade.TABLE_NAME)
@TableName(MemberGrade.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = MemberGrade.TABLE_NAME, comment = "会员等级表")
public class MemberGrade extends BaseSuperEntity<MemberGrade, Long> {

	public static final String TABLE_NAME = "tt_member_grade";

	/**
	 * 等级名称
	 */
	@Column(name = "grade_name", nullable = false, columnDefinition = "varchar(64) not null comment '等级名称'")
	private String gradeName;

	/**
	 * 等级图片
	 */
	@Column(name = "grade_image", nullable = false, columnDefinition = "varchar(1024) not null comment '等级图片'")
	private String gradeImage;

	/**
	 * 所需经验值
	 */
	@Column(name = "experience_value", nullable = false, columnDefinition = "int not null default 0 comment '所需经验值'")
	private Integer experienceValue;

	/**
	 * 是否为默认等级
	 */
	@Column(name = "defaulted", nullable = false, columnDefinition = "boolean not null default false comment '是否为默认等级'")
	private Boolean defaulted;

	public String getGradeName() {
		return gradeName;
	}

	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}

	public String getGradeImage() {
		return gradeImage;
	}

	public void setGradeImage(String gradeImage) {
		this.gradeImage = gradeImage;
	}

	public Integer getExperienceValue() {
		return experienceValue;
	}

	public void setExperienceValue(Integer experienceValue) {
		this.experienceValue = experienceValue;
	}

	public Boolean getDefaulted() {
		return defaulted;
	}

	public void setDefaulted(Boolean defaulted) {
		this.defaulted = defaulted;
	}
}
