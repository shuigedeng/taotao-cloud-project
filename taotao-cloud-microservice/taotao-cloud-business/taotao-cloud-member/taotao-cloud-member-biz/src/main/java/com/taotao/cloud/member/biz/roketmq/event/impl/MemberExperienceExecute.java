package com.taotao.cloud.member.biz.roketmq.event.impl;


import com.taotao.cloud.member.api.enums.PointTypeEnum;
import com.taotao.cloud.member.biz.model.entity.Member;
import com.taotao.cloud.member.biz.roketmq.event.MemberRegisterEvent;
import com.taotao.cloud.member.biz.service.business.IMemberService;
import com.taotao.cloud.order.api.feign.IFeignOrderApi;
import com.taotao.cloud.sys.api.enums.SettingCategoryEnum;
import com.taotao.cloud.sys.api.feign.IFeignSettingApi;
import com.taotao.cloud.sys.api.model.vo.setting.ExperienceSettingVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 会员经验值
 */
@Service
public class MemberExperienceExecute implements MemberRegisterEvent {

	/**
	 * 配置
	 */
	@Autowired
	private IFeignSettingApi settingApi;
	/**
	 * 会员
	 */
	@Autowired
	private IMemberService memberService;
	/**
	 * 订单
	 */
	@Autowired
	private IFeignOrderApi orderApi;

	/**
	 * 会员注册赠送经验值
	 *
	 * @param member 会员
	 */
	@Override
	public void memberRegister(Member member) {
		//获取经验值设置
		ExperienceSettingVO experienceSetting = getExperienceSetting();
		//赠送会员经验值
		memberService.updateMemberPoint(experienceSetting.getRegister().longValue(),
			PointTypeEnum.INCREASE.name(), member.getId(),
			"会员注册，赠送经验值" + experienceSetting.getRegister());
	}

	/**
	 * 获取经验值设置
	 *
	 * @return 经验值设置
	 */
	private ExperienceSettingVO getExperienceSetting() {
		ExperienceSettingVO setting = settingApi.getExperienceSetting(
			SettingCategoryEnum.EXPERIENCE_SETTING.name());
		return setting;
	}
}
