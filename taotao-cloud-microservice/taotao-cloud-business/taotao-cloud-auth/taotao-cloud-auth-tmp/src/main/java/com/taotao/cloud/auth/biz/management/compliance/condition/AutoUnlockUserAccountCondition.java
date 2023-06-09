/*
 * Copyright (c) 2020-2030 ZHENGGENGWEI(码匠君)<herodotus@aliyun.com>
 *
 * Dante Engine licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Dante Engine 采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改 Dante Cloud 源码头部的版权声明。
 * 3.请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 
 * 5.在修改包名，模块名称，项目代码等时，请注明软件出处 
 * 6.若您的项目无法满足以上几点，可申请商业授权
 */

package com.taotao.cloud.auth.biz.management.compliance.condition;

import com.taotao.cloud.security.springsecurity.core.constants.OAuth2Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * <p>Description: 阿里云短信开启条件 </p>
 *
 * @author : gengwei.zheng
 * @date : 2022/1/27 16:23
 */
public class AutoUnlockUserAccountCondition implements Condition {

	private static final Logger log = LoggerFactory.getLogger(AutoUnlockUserAccountCondition.class);

	@SuppressWarnings("NullableProblems")
	@Override
	public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata metadata) {
//		boolean result = PropertyResolver.getBoolean(conditionContext, OAuth2Constants.ITEM_COMPLIANCE_AUTO_UNLOCK, true);
//		log.debug("[Herodotus] |- Condition [Auto Unlock User Account] value is [{}]", result);
//		return result;
		return true;
	}
}
