package com.taotao.cloud.member.api.model.vo;

/**
 * 会员等级表
 *
 * @param gradeName       等级名称
 * @param gradeImage      等级图片
 * @param experienceValue 所需经验值
 * @param defaulted       是否为默认等级
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-03-11 15:16:55
 */
public record MemberGradeVO(String gradeName, String gradeImage, Integer experienceValue,
                            Boolean defaulted) {


}
