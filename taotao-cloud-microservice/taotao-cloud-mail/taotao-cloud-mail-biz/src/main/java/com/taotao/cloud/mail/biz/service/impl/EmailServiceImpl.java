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
package com.taotao.cloud.mail.biz.service.impl;

import com.taotao.cloud.common.enums.ResultEnum;
import com.taotao.cloud.common.exception.BusinessException;
import com.taotao.cloud.mail.biz.entity.Email;
import com.taotao.cloud.mail.biz.repository.EmailRepository;
import com.taotao.cloud.mail.biz.service.IEmailService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author dengtao
 * @since 2020/11/13 10:00
 * @version 1.0.0
 */
@Service
@AllArgsConstructor
public class EmailServiceImpl implements IEmailService {

	private final EmailRepository emailRepository;

	@Override
	public Email findEmailById(Long id) {
		Optional<Email> optionalExpressCompany = emailRepository.findById(id);
		return optionalExpressCompany.orElseThrow(() -> new BusinessException(ResultEnum.EMAIL_NOT_EXIST));
	}
}
