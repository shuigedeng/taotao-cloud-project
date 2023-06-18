/*
 * MIT License
 * Copyright (c) 2020-2029 YongWu zheng (dcenter.top and gitee.com/pcore and github.com/ZeroOrInfinity)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.taotao.cloud.auth.biz.authentication.login.extension.justauth.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.SpringSecurityCoreVersion;

import java.io.Serializable;

/**
 * 查询本地账号下的第三方绑定账号 dto
 * @author YongWu zheng
 * @weixin z56133
 * @since 2021.3.3 20:03
 */
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConnectionDto implements Serializable {
	private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

	/**
	 * 本地主键 id
	 */
	private String id;
	/**
	 * 本地用户 id
	 */
	private String userId;
	/**
	 * 第三方服务商 id
	 */
	private String providerId;
	/**
	 * 第三方用户 id
	 */
	private String providerUserId;

	/**
	 * 第三方用户 token id
	 */
	private Long tokenId;

}
