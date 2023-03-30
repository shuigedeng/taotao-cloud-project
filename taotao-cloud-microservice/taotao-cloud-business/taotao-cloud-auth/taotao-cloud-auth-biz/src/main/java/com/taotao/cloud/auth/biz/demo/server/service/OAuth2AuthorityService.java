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

package com.taotao.cloud.auth.biz.demo.server.service;

import cn.herodotus.engine.data.core.repository.BaseRepository;
import cn.herodotus.engine.data.core.service.BaseLayeredService;
import cn.herodotus.engine.oauth2.server.authentication.entity.OAuth2Authority;
import cn.herodotus.engine.oauth2.server.authentication.repository.OAuth2AuthorityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Description: OAuth2AuthorityService
 *
 * @author : gengwei.zheng
 * @date : 2022/4/1 13:53
 */
@Service
public class OAuth2AuthorityService extends BaseLayeredService<OAuth2Authority, String> {

    private final OAuth2AuthorityRepository authorityRepository;

    @Autowired
    public OAuth2AuthorityService(OAuth2AuthorityRepository authorityRepository) {
        this.authorityRepository = authorityRepository;
    }

    @Override
    public BaseRepository<OAuth2Authority, String> getRepository() {
        return authorityRepository;
    }
}
