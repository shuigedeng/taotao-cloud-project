/*
 * Copyright (c) 2020-2030 ZHENGGENGWEI(码匠君)<herodotus@aliyun.com>
 *
 * Dante Engine Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
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
 * 2.请不要删除和修改 Dante Engine 源码头部的版权声明。
 * 3.请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 https://gitee.com/herodotus/dante-engine
 * 5.在修改包名，模块名称，项目代码等时，请注明软件出处 https://gitee.com/herodotus/dante-engine
 * 6.若您的项目无法满足以上几点，可申请商业授权
 */

package com.taotao.cloud.auth.biz.oauth2.jpa.generator;

import com.taotao.cloud.auth.biz.oauth2.jpa.entity.HerodotusRegisteredClient;
import java.io.Serializable;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.UUIDGenerator;

/**
 * <p>Description: OAuth2RegisteredClient Id 生成器 </p>
 * <p>
 * 指定ID生成器，解决实体ID无法手动设置问题。
 *
 * @author : gengwei.zheng
 * @date : 2022/1/22 17:50
 */
public class HerodotusRegisteredClientGenerator extends UUIDGenerator {

	@Override
	public Serializable generate(SharedSessionContractImplementor session, Object object)
		throws HibernateException {
		if (ObjectUtils.isEmpty(object)) {
			throw new HibernateException(new NullPointerException());
		}

		HerodotusRegisteredClient oAuthHerodotusRegisteredClient = (HerodotusRegisteredClient) object;

		if (StringUtils.isEmpty(oAuthHerodotusRegisteredClient.getId())) {
			return super.generate(session, object);
		} else {
			return oAuthHerodotusRegisteredClient.getId();
		}
	}
}
