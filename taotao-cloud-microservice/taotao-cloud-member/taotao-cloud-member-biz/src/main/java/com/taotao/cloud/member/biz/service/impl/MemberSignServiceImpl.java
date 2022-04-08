package com.taotao.cloud.member.biz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.taotao.cloud.common.enums.ResultEnum;
import com.taotao.cloud.common.exception.BusinessException;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.common.utils.bean.BeanUtil;
import com.taotao.cloud.common.utils.common.SecurityUtil;
import com.taotao.cloud.common.utils.date.DateUtil;
import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.common.utils.number.CurrencyUtil;
import com.taotao.cloud.member.api.enums.PointTypeEnum;
import com.taotao.cloud.member.api.vo.MemberSignVO;
import com.taotao.cloud.member.biz.entity.MemberSign;
import com.taotao.cloud.member.biz.mapper.MemberSignMapper;
import com.taotao.cloud.member.biz.service.MemberService;
import com.taotao.cloud.member.biz.service.MemberSignService;
import com.taotao.cloud.sys.api.enums.SettingEnum;
import com.taotao.cloud.sys.api.feign.IFeignSettingService;
import com.taotao.cloud.sys.api.setting.PointSetting;
import com.taotao.cloud.sys.api.setting.PointSettingItem;
import com.taotao.cloud.sys.api.vo.setting.SettingVO;
import java.util.Date;
import java.util.List;

import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 会员签到业务层实现
 */
@Service
public class MemberSignServiceImpl extends ServiceImpl<MemberSignMapper, MemberSign> implements
	MemberSignService {

	/**
	 * RocketMQ
	 */
	@Autowired
	private RocketMQTemplate rocketMQTemplate;
	/**
	 * RocketMQ 配置
	 */
	@Autowired
	private RocketmqCustomProperties rocketmqCustomProperties;
	/**
	 * 配置
	 */
	@Autowired
	private IFeignSettingService feignSettingService;
	/**
	 * 会员
	 */
	@Autowired
	private MemberService memberService;

	@Override
	public Boolean memberSign() {
		//获取当前会员信息
		QueryWrapper<MemberSign> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("member_id", SecurityUtil.getUserId());
		queryWrapper.between("create_time", new Date(DateUtil.startOfTodDay() * 1000),
			DateUtil.getCurrentDayEndTime());

		//校验今天是否已经签到
		List<MemberSign> todaySigns = this.baseMapper.getTodayMemberSign(queryWrapper);
		if (todaySigns.size() > 0) {
			throw new BusinessException(ResultEnum.MEMBER_SIGN_REPEAT);
		}
		//当前签到天数的前一天日期
		List<MemberSign> signs = this.baseMapper.getBeforeMemberSign(SecurityUtil.getUserId());
		//构建参数
		MemberSign memberSign = new MemberSign();
		memberSign.setMemberId(SecurityUtil.getUserId());
		memberSign.setMemberName(SecurityUtil.getUsername());

		//如果size大于0 说明昨天已经签到过，获取昨天的签到数，反之新签到
		if (signs.size() > 0) {
			//截止目前为止 签到总天数 不带今天
			Integer signDay = signs.get(0).getSignDay();
			memberSign.setSignDay(CurrencyUtil.add(signDay, 1).intValue());
		} else {
			memberSign.setSignDay(1);
		}

		int result = this.baseMapper.insert(memberSign);
		//签到成功后发送消息赠送积分
		if (result > 0) {
			//String destination = rocketmqCustomProperties.getMemberTopic() + ":"
			//	+ MemberTagsEnum.MEMBER_SING.name();
			//rocketMQTemplate.asyncSend(destination, memberSign,
			//	RocketmqSendCallbackBuilder.commonCallback());
			return true;
		}
		return false;
	}

	@Override
	public List<MemberSignVO> getMonthSignDay(String time) {
		List<MemberSign> monthMemberSign = this.baseMapper.getMonthMemberSign(
			SecurityUtil.getUserId(), time);
		return BeanUtil.copy(monthMemberSign, MemberSignVO.class);
	}

	@Override
	public void memberSignSendPoint(String memberId, Integer day) {
		try {
			//获取签到积分赠送设置
			Result<SettingVO> settingResult = feignSettingService.get(
				SettingEnum.POINT_SETTING.name());
			SettingVO setting = settingResult.data();
			if (setting != null) {
				PointSetting pointSetting = new Gson().fromJson(setting.getSettingValue(),
					PointSetting.class);
				String content = "";
				//赠送积分
				Long point = null;
				List<PointSettingItem> pointSettingItems = pointSetting.getPointSettingItems();
				if (!pointSettingItems.isEmpty()) {
					for (PointSettingItem item : pointSettingItems) {
						if (item.getDay().equals(day)) {
							point = item.getPoint().longValue();
							content = "会员连续签到" + day + "天，赠送积分" + point + "分";
						}
					}
				}
				//如果他不处于连续赠送阶段，则只赠送签到积分数
				if (point == null && pointSetting.getSignIn() != null) {
					point = Long.valueOf(pointSetting.getSignIn().toString());
					content = "会员签到第" + day + "天，赠送积分" + point + "分";
				}
				//赠送会员积分
				memberService.updateMemberPoint(point, PointTypeEnum.INCREASE.name(), memberId,
					content);
			}
		} catch (Exception e) {
			LogUtil.error("会员签到错误", e);
		}
	}

}
