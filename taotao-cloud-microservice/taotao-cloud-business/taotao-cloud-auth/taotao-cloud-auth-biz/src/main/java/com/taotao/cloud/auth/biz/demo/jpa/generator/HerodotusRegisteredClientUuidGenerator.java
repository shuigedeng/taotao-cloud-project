/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
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

package com.taotao.cloud.auth.biz.demo.jpa.generator;

import cn.herodotus.engine.data.jpa.hibernate.identifier.AbstractUuidGenerator;
import cn.herodotus.engine.oauth2.data.jpa.entity.HerodotusRegisteredClient;
import java.lang.reflect.Member;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.factory.spi.CustomIdGeneratorCreationContext;

/**
 * Description: OAuth2RegisteredClient Id 生成器
 *
 * <p>指定ID生成器，解决实体ID无法手动设置问题。
 *
 * @author : gengwei.zheng
 * @date : 2022/1/22 17:50
 */
public class HerodotusRegisteredClientUuidGenerator extends AbstractUuidGenerator {

    public HerodotusRegisteredClientUuidGenerator(
            HerodotusRegisteredClientUuid config, Member idMember, CustomIdGeneratorCreationContext creationContext) {
        super(idMember);
    }

    @Override
    public Object generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        if (ObjectUtils.isEmpty(object)) {
            throw new HibernateException(new NullPointerException());
        }

        HerodotusRegisteredClient herodotusRegisteredClient = (HerodotusRegisteredClient) object;

        if (StringUtils.isEmpty(herodotusRegisteredClient.getId())) {
            return super.generate(session, object);
        } else {
            return herodotusRegisteredClient.getId();
        }
    }
}
