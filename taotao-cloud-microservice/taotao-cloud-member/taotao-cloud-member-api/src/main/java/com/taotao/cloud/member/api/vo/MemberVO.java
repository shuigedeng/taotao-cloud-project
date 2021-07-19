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
package com.taotao.cloud.member.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 会员信息VO
 *
 * @author shuigedeng
 * @since 2020/11/20 上午9:42
 * @version 1.0.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "会员信息VO", description = "会员信息VO")
public class MemberVO implements Serializable {
	private static final long serialVersionUID = 5126530068827085130L;

	@ApiModelProperty(value = "id")
	private Long id;

	@ApiModelProperty(value = "昵称")
	private String nickname;

	@ApiModelProperty(value = "用户名")
	private String username;

	@ApiModelProperty(value = "手机号")
	private String phone;

	@ApiModelProperty(value = "密码")
	private String password;

	@ApiModelProperty(value = "头像")
	private String avatar;

	@ApiModelProperty(value = "性别")
	private Byte gender;

	@ApiModelProperty(value = "邮箱")
	private String email;

	@ApiModelProperty(value = "等级")
	private Integer level;

	@ApiModelProperty(value = "用户类型")
	private Integer type;

	@ApiModelProperty(value = "创建ip")
	private String createIp;

	@ApiModelProperty(value = "最后一次登陆时间")
	private LocalDateTime lastLoginTime;

	@ApiModelProperty(value = "最后一次登陆ip")
	private String lastLoginIp;

	@ApiModelProperty(value = "是否锁定")
	private Integer isLock;

	@ApiModelProperty(value = "状态 1:启用, 0:停用")
	private Integer status;

	@ApiModelProperty(value = "登录次数")
	private Integer loginTimes;

	@ApiModelProperty(value = "省code")
	private String provinceCode;

	@ApiModelProperty(value = "市code")
	private String cityCode;

	@ApiModelProperty(value = "区、县code")
	private String areaCode;

	@ApiModelProperty(value = "创建时间")
	private LocalDateTime createTime;

	@ApiModelProperty(value = "最后修改时间")
	private LocalDateTime lastModifiedTime;
}
