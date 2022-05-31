package com.taotao.cloud.member.api.vo;

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
public class MemberGradeVO {


	/**
	 * 等级名称
	 */
	private String gradeName;

	/**
	 * 等级图片
	 */
	private String gradeImage;

	/**
	 * 所需经验值
	 */
	private Integer experienceValue;

	/**
	 * 是否为默认等级
	 */
	private Boolean defaulted;
}
