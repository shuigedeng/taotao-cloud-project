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

package com.taotao.cloud.auth.biz.authentication.login.extension.justauth.repository.factory;

import com.taotao.cloud.auth.biz.authentication.login.extension.justauth.properties.RepositoryProperties;
import com.taotao.cloud.auth.biz.authentication.login.extension.justauth.repository.UsersConnectionRepository;
import com.taotao.cloud.auth.biz.authentication.login.extension.justauth.repository.jdbc.Auth2JdbcUsersConnectionRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.encrypt.TextEncryptor;

/**
 * UsersConnectionRepositoryFactory 接口实现，
 * 用户需要对第三方{@link RepositoryProperties} <i>userConnectionTableName</i> 用户表更改或者更改Repository的实现方式（如更换Redis）时，要实现此接口
 * {@link UsersConnectionRepositoryFactory}
 * .<br><br>
 * 自定义的接口实现并注入 IOC 容器会自动覆盖此类
 *
 * @author YongWu zheng
 * @version V2.0  Created by 2020/5/13 23:37
 */
public class Auth2JdbcUsersConnectionRepositoryFactory implements UsersConnectionRepositoryFactory {

    @Override
    public UsersConnectionRepository getUsersConnectionRepository(
            JdbcTemplate auth2UserConnectionJdbcTemplate,
            TextEncryptor textEncryptor,
            RepositoryProperties repositoryProperties) {
        return new Auth2JdbcUsersConnectionRepository(
                auth2UserConnectionJdbcTemplate, textEncryptor, repositoryProperties);
    }
}
