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
package com.taotao.cloud.dubbo.order.provider;

import com.alibaba.dubbo.config.annotation.Reference;
import com.taotao.cloud.dubbo.account.api.AccountService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * DubboController
 *
 * @author dengtao
 * @version v1.0
 * @since 2021/02/24 11:35
 */
public class DubboController {

	@Reference
	private AccountService accountService;

	@GetMapping("/order/getAccount/{accountCode}")
	public String getAccount(@PathVariable String accountCode){

		return accountService.getByCode(accountCode);
	}

}
