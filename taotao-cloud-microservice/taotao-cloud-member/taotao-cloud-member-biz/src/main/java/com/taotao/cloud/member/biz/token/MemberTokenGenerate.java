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
package com.taotao.cloud.member.biz.token;

import com.taotao.cloud.common.enums.ClientTypeEnum;
import com.taotao.cloud.common.enums.UserEnum;
import com.taotao.cloud.common.utils.servlet.RequestUtils;
import com.taotao.cloud.member.biz.connect.token.Token;
import com.taotao.cloud.member.biz.connect.token.TokenUtil;
import com.taotao.cloud.member.biz.connect.token.base.AbstractTokenGenerate;
import com.taotao.cloud.member.biz.model.entity.Member;
import com.taotao.cloud.stream.properties.RocketmqCustomProperties;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 会员token生成
 */
@Component
public class MemberTokenGenerate extends AbstractTokenGenerate<Member> {
	@Autowired
	private TokenUtil tokenUtil;
	@Autowired
	private RocketmqCustomProperties rocketmqCustomProperties;

	@Autowired
	private RocketMQTemplate rocketMQTemplate;

	@Override
	public Token createToken(Member member, Boolean longTerm) {
		//获取客户端类型
		String clientType = RequestUtils.getRequest().getHeader("clientType");
		ClientTypeEnum clientTypeEnum;
		try {
			//如果客户端为空，则缺省值为PC，pc第三方登录时不会传递此参数
			if (clientType == null) {
				clientTypeEnum = ClientTypeEnum.PC;
			} else {
				clientTypeEnum = ClientTypeEnum.valueOf(clientType);
			}
		} catch (IllegalArgumentException e) {
			clientTypeEnum = ClientTypeEnum.UNKNOWN;
		}
		//记录最后登录时间，客户端类型
		member.setLastLoginDate(LocalDateTime.now());
		member.setClient(clientTypeEnum.name());
		// String destination = rocketmqCustomProperties.getMemberTopic() + ":" + MemberTagsEnum.MEMBER_LOGIN.name();
		// rocketMQTemplate.asyncSend(destination, member, RocketmqSendCallbackBuilder.commonCallback());
		//
		// AuthUser authUser = new AuthUser(member.getUsername(), member.getId(), member.getNickName(), member.getFace(), UserEnum.MEMBER);
		// //登陆成功生成token
		// return tokenUtil.createToken(member.getUsername(), authUser, longTerm, UserEnum.MEMBER);

		return null;
	}

	@Override
	public Token refreshToken(String refreshToken) {
		return tokenUtil.refreshToken(refreshToken, UserEnum.MEMBER);
	}

}
