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
package com.taotao.cloud.dubbo.account.provider;

import com.alibaba.dubbo.config.annotation.Service;
import com.taotao.cloud.dubbo.account.api.AccountService;

/**
 * AccountServiceImpl
 *
 * @author dengtao
 * @version v1.0
 * @since 2021/02/24 11:34
 */
@Service
public class AccountServiceImpl implements AccountService {

	@Override
	public String getByCode(String accountCode) {
		return "123123132123123------" + accountCode;
	}
}
