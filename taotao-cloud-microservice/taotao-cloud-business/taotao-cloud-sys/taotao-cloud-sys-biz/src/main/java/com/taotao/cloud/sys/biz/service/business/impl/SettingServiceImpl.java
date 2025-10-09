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

package com.taotao.cloud.sys.biz.service.business.impl;

import com.taotao.cloud.sys.biz.mapper.ISettingMapper;
import com.taotao.cloud.sys.biz.model.entity.setting.Setting;
import com.taotao.cloud.sys.biz.repository.SettingRepository;
import com.taotao.cloud.sys.biz.repository.ISettingRepository;
import com.taotao.cloud.sys.biz.service.business.ISettingService;
import com.taotao.boot.webagg.service.impl.BaseSuperServiceImpl;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * SettingServiceImpl
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2022/03/10 10:31
 */
@Service
@CacheConfig(cacheNames = "{setting}")
public class SettingServiceImpl
        extends BaseSuperServiceImpl< Setting, Long, ISettingMapper,SettingRepository, ISettingRepository>
        implements ISettingService {

    @Override
    @Cacheable(key = "#key")
    public Setting get(String key) {
        return this.getById(key);
    }

    @Override
    @CacheEvict(key = "#setting.id")
    public boolean saveUpdate(Setting setting) {
        return this.saveOrUpdate(setting);
    }
}
