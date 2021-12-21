package com.taotao.cloud.member.biz.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 会员等级
 *
 * @since 2021/5/14 5:43 下午
 */
@Entity
@Table(name = MemberGrade.TABLE_NAME)
@TableName(MemberGrade.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = MemberGrade.TABLE_NAME, comment = "会员等级表")
public class MemberGrade extends BaseSuperEntity<MemberGrade, Long> {

	public static final String TABLE_NAME = "li_member_grade";


	@Column(name = "grade_name", nullable = false, columnDefinition = "varchar(32) not null comment '等级名称'")
	private String gradeName;


	@Column(name = "grade_image", nullable = false, columnDefinition = "varchar(32) not null comment '等级图片'")
	private String gradeImage;


	@Column(name = "experience_value", nullable = false, columnDefinition = "int not null default 0 comment '所需经验值'")
	private Integer experienceValue;

	@Column(name = "is_default", nullable = false, columnDefinition = "boolean not null default false comment '是否为默认等级'")
	private Boolean isDefault;

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

	public Boolean getDefault() {
		return isDefault;
	}

	public void setDefault(Boolean aDefault) {
		isDefault = aDefault;
	}
}
