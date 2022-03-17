package com.taotao.cloud.member.biz.roketmq.event.impl;


import com.google.gson.Gson;
import com.taotao.cloud.member.api.enums.PointTypeEnum;
import com.taotao.cloud.member.biz.entity.Member;
import com.taotao.cloud.member.biz.roketmq.event.MemberRegisterEvent;
import com.taotao.cloud.member.biz.service.MemberService;
import com.taotao.cloud.sys.api.enums.SettingEnum;
import com.taotao.cloud.sys.api.setting.ExperienceSetting;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 会员经验值
 */
//@Service
public class MemberExperienceExecute implements MemberRegisterEvent {

	/**
	 * 配置
	 */
	@Autowired
	private SettingService settingService;
	/**
	 * 会员
	 */
	@Autowired
	private MemberService memberService;
	/**
	 * 订单
	 */
	@Autowired
	private OrderService orderService;

	/**
	 * 会员注册赠送经验值
	 *
	 * @param member 会员
	 */
	@Override
	public void memberRegister(Member member) {
		//获取经验值设置
		ExperienceSetting experienceSetting = getExperienceSetting();
		//赠送会员经验值
		memberService.updateMemberPoint(Long.valueOf(experienceSetting.getRegister().longValue()),
			PointTypeEnum.INCREASE.name(), member.getId(),
			"会员注册，赠送经验值" + experienceSetting.getRegister());
	}

	/**
	 * 获取经验值设置
	 *
	 * @return 经验值设置
	 */
	private ExperienceSetting getExperienceSetting() {
		Setting setting = settingService.get(SettingEnum.EXPERIENCE_SETTING.name());
		return new Gson().fromJson(setting.getSettingValue(), ExperienceSetting.class);
	}
}
