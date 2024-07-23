/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.member.application.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.common.utils.bean.BeanUtils;
import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.member.application.service.IMemberService;
import com.taotao.cloud.member.application.service.IMemberSignService;
import com.taotao.cloud.member.common.enums.SettingCategoryEnum;
import com.taotao.cloud.member.infrastructure.persistent.mapper.IMemberSignMapper;
import com.taotao.cloud.member.infrastructure.persistent.po.MemberSignPO;
import com.taotao.cloud.mq.stream.framework.rocketmq.RocketmqSendCallbackBuilder;
import com.taotao.cloud.mq.stream.framework.rocketmq.tags.MemberTagsEnum;
import com.taotao.cloud.mq.stream.properties.RocketmqCustomProperties;
import com.taotao.cloud.security.springsecurity.utils.SecurityUtils;
import java.util.List;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 会员签到业务层实现
 */
@Service
public class MemberSignServiceImpl extends ServiceImpl<IMemberSignMapper, MemberSignPO> implements
	IMemberSignService {

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
	private IFeignSettingApi settingApi;
	/**
	 * 会员
	 */
	@Autowired
	private IMemberService memberService;

	@Override
	public Boolean memberSign() {
		// 获取当前会员信息
		AuthUser authUser = UserContext.getCurrentUser();
		if (authUser != null) {

			LambdaQueryWrapper<MemberSignPO> queryWrapper = new LambdaQueryWrapper<>();
			queryWrapper.eq(MemberSignPO::getMemberId, authUser.getId());
			List<MemberSignPO> signSize = this.baseMapper.getTodayMemberSign(queryWrapper);
			if (signSize.size() > 0) {
				throw new ServiceException(ResultCode.MEMBER_SIGN_REPEAT);
			}
			// 当前签到天数的前一天日期
			List<MemberSignPO> signs = this.baseMapper.getBeforeMemberSign(authUser.getId());
			// 构建参数
			MemberSignPO memberSignPO = new MemberSignPO();
			memberSignPO.setMemberId(authUser.getId());
			memberSignPO.setMemberName(authUser.getUsername());
			// 如果size大于0 说明昨天已经签到过，获取昨天的签到数，反之新签到
			if (!signs.isEmpty()) {
				// 截止目前为止 签到总天数 不带今天
				Integer signDay = signs.get(0).getSignDay();
				memberSignPO.setSignDay(CurrencyUtil.add(signDay, 1).intValue());
			}
			else {
				memberSignPO.setSignDay(1);
			}

			memberSignPO.setDay(DateUtil.getDayOfStart().intValue());
			try {
				this.baseMapper.insert(memberSignPO);
				// 签到成功后发送消息赠送积分
				String destination =
					rocketmqCustomProperties.getMemberTopic() + ":"
						+ MemberTagsEnum.MEMBER_SING.name();
				rocketMQTemplate.asyncSend(destination, memberSignPO,
					RocketmqSendCallbackBuilder.commonCallback());
				return true;
			}
			catch (Exception e) {
				throw new ServiceException(ResultCode.MEMBER_SIGN_REPEAT);
			}
		}
		throw new ServiceException(ResultCode.USER_NOT_LOGIN);
	}

	@Override
	public List<MemberSignVO> getMonthSignDay(String time) {
		List<MemberSignPO> monthMemberSignPO = this.baseMapper.getMonthMemberSign(
			SecurityUtils.getUserId(), time);
		return BeanUtils.copy(monthMemberSignPO, MemberSignVO.class);
	}

	@Override
	public void memberSignSendPoint(Long memberId, Integer day) {
		try {
			// 获取签到积分赠送设置
			PointSettingVO pointSetting = settingApi.getPointSetting(
				SettingCategoryEnum.POINT_SETTING.name());
			String content = "";
			// 赠送积分
			Long point = null;
			List<PointSettingItemVO> pointSettingItems = pointSetting.getPointSettingItems();
			if (!pointSettingItems.isEmpty()) {
				for (PointSettingItemVO item : pointSettingItems) {
					if (item.getDay().equals(day)) {
						point = item.getPoint().longValue();
						content = "会员连续签到" + day + "天，赠送积分" + point + "分";
					}
				}
			}
			// 如果他不处于连续赠送阶段，则只赠送签到积分数
			if (point == null && pointSetting.getSignIn() != null) {
				point = Long.valueOf(pointSetting.getSignIn().toString());
				content = "会员签到第" + day + "天，赠送积分" + point + "分";
			}
			// 赠送会员积分
			memberService.updateMemberPoint(point, PointTypeEnum.INCREASE.name(), memberId,
				content);
		}
		catch (Exception e) {
			LogUtils.error("会员签到错误", e);
		}
	}
}
