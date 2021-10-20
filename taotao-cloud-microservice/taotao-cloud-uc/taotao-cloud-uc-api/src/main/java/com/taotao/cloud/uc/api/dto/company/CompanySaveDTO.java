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
package com.taotao.cloud.uc.api.dto.company;


import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import javax.validation.constraints.Pattern;

/**
 * 公司新增对象
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-10-09 16:31:52
 */
@Schema(description = "公司新增对象")
public record CompanySaveDTO(

	/**
	 * 租户id
	 */
	@Schema(description = "租户id")
	String tenantId,

	/**
	 * 租户密钥
	 */
	@Schema(description = "租户密钥")
	String tenantSecret,

	/**
	 * 公司名称
	 */
	@Schema(description = "公司名称")
	String name,

	/**
	 * 企业全称
	 */
	@Schema(description = "企业全称")
	String fullName,

	/**
	 * 信用代码
	 */
	@Pattern(regexp = "^|[a-zA-Z0-9]{18}$", message = "信用代码格式错误")
	@Schema(description = "信用代码")
	String creditCode,

	/**
	 * 邮箱
	 */
	@Schema(description = "邮箱")
	String email,

	/**
	 * 联系人
	 */
	@Schema(description = "联系人")
	String username,

	/**
	 * 联系人手机号
	 */
	@Schema(description = "联系人手机号")
	String phone,

	/**
	 * 联系人地址
	 */
	@Schema(description = "联系人地址")
	String address,

	/**
	 * 请求域名
	 */
	@Schema(description = "请求域名")
	String domain,

	/**
	 * 公司网址
	 */
	@Schema(description = "公司网址")
	String webSite,

	/**
	 * 所在地区
	 */
	@Schema(description = "所在地区")
	String regionInfo,

	/**
	 * 公司类型
	 */
	@Schema(description = "公司类型")
	Integer type) implements Serializable {

	static final long serialVersionUID = 5126530068827085130L;


}
