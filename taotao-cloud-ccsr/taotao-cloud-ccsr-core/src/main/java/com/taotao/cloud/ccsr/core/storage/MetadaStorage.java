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

package com.taotao.cloud.ccsr.core.storage;

import com.taotao.cloud.ccsr.api.grpc.auto.Metadata;
import com.taotao.cloud.ccsr.spi.Join;
import com.taotao.boot.common.utils.lang.StringUtils;

/**
 * MetadaStorage
 *
 * @author shuigedeng
 * @version 2026.01
 * @since 2025-12-19 09:30:45
 */
@Join(order = 1, isSingleton = true)
public class MetadaStorage extends AbstractStorage<Metadata> {

    public String key( String namespace, String group, String tag, String dataId ) {
        StringBuilder builder = new StringBuilder();
        if (StringUtils.isNotBlank(namespace)) {
            builder.append(namespace).append("#");
        }
        if (StringUtils.isNotBlank(group)) {
            builder.append(group).append("#");
        }
        if (StringUtils.isNotBlank(tag)) {
            builder.append(tag).append("#");
        }
        if (StringUtils.isNotBlank(dataId)) {
            builder.append(dataId).append("#");
        }
        // Remove trailing '#' if present
        if (!builder.isEmpty() && builder.charAt(builder.length() - 1) == '#') {
            builder.deleteCharAt(builder.length() - 1);
        }
        return builder.toString();
    }

    @Override
    public String key( Metadata data ) {
        return key(data.getNamespace(), data.getGroup(), data.getTag(), data.getDataId());
    }

    @Override
    public boolean check( Metadata data ) throws IllegalArgumentException {
        if (data == null) {
            throw new IllegalArgumentException("data is null");
        }
        if (StringUtils.isBlank(data.getNamespace())) {
            throw new IllegalArgumentException("namespace is null");
        }
        if (StringUtils.isBlank(data.getGroup())) {
            throw new IllegalArgumentException("group is null");
        }
        if (StringUtils.isBlank(data.getDataId())) {
            throw new IllegalArgumentException("dataId is null");
        }
        if (StringUtils.isBlank(data.getContent())) {
            throw new IllegalArgumentException("content is null");
        }
        if (super.limit(data.getContent())) {
            throw new IllegalArgumentException("content is too large");
        }
        return true;
    }
}
