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

import com.taotao.cloud.common.enums.ResultEnum;
import com.taotao.cloud.common.enums.UserEnum;
import com.taotao.cloud.common.exception.BusinessException;
import com.taotao.cloud.member.biz.connect.token.Token;
import com.taotao.cloud.member.biz.connect.token.TokenUtil;
import com.taotao.cloud.member.biz.connect.token.base.AbstractTokenGenerate;
import com.taotao.cloud.member.biz.model.entity.Member;
import com.taotao.cloud.store.api.feign.IFeignStoreApi;
import com.taotao.cloud.store.api.model.vo.StoreVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 商家token生成
 */
@Component
public class StoreTokenGenerate extends AbstractTokenGenerate<Member> {
	@Autowired
	private IFeignStoreApi storeService;
	@Autowired
	private TokenUtil tokenUtil;

	@Override
	public Token createToken(Member member, Boolean longTerm) {
		if (Boolean.FALSE.equals(member.getHaveStore())) {
			throw new BusinessException(ResultEnum.STORE_NOT_OPEN);
		}
		StoreVO store = storeService.findSotreByMemberId(member.getId());
		// AuthUser authUser = new AuthUser(member.getUsername(), member.getId(), member.getNickname(), store.getStoreLogo(), UserEnum.STORE);
		//
		// authUser.setStoreId(store.getId());
		// authUser.setStoreName(store.getStoreName());
		// return tokenUtil.createToken(member.getUsername(), authUser, longTerm, UserEnum.STORE);

		return null;
	}

	@Override
	public Token refreshToken(String refreshToken) {
		return tokenUtil.refreshToken(refreshToken, UserEnum.STORE);
	}

}
