/*
 * Copyright 2002-2021 the original author or authors.
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
package com.taotao.cloud.log.biz.service.impl;

import com.taotao.cloud.common.enums.ResultEnum;
import com.taotao.cloud.common.exception.BusinessException;
import com.taotao.cloud.log.biz.entity.MemberLogin;
import com.taotao.cloud.log.biz.repository.MemberLoginRepository;
import com.taotao.cloud.log.biz.service.IMemberLoginService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author dengtao
 * @date 2020/11/13 10:00
 * @since v1.0
 */
@Service
@AllArgsConstructor
public class MemberLoginServiceImpl implements IMemberLoginService {

	private final MemberLoginRepository memberLoginRepository;

	@Override
	public MemberLogin findMemberLoginById(Long id) {
		Optional<MemberLogin> optionalMemberLogin = memberLoginRepository.findById(id);
		return optionalMemberLogin.orElseThrow(() -> new BusinessException(ResultEnum.FILE_NOT_EXIST));
	}
}
