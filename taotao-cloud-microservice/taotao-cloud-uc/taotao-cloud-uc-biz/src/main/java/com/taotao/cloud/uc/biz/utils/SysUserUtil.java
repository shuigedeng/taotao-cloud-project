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
package com.taotao.cloud.uc.biz.utils;

import com.taotao.cloud.common.utils.BeanUtil;
import com.taotao.cloud.uc.api.vo.user.UserVO;
import com.taotao.cloud.uc.biz.entity.SysUser;

/**
 * @author dengtao
 * @since 2020/10/20 16:16
 * @version 1.0.0
 */
public class SysUserUtil {
	public static UserVO copy(SysUser user) {
		UserVO vo = UserVO.builder().build();
		BeanUtil.copyIncludeNull(user, vo);
		return vo;
	}
}
