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

package com.taotao.cloud.sys.biz.largefile.context;

import com.taotao.cloud.file.biz.largefile.annotation.UploadMode;
import com.taotao.cloud.file.biz.largefile.enu.UploadModeEnum;
import com.taotao.cloud.file.biz.largefile.strategy.SliceUploadStrategy;
import com.taotao.cloud.file.biz.largefile.util.SpringContextHolder;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.util.Assert;

public enum UploadContext {
    INSTANCE;

    private static final String PACKAGE_NAME = "com.github.lybgeek.upload.strategy.impl";

    private Map<UploadModeEnum, Class<SliceUploadStrategy>> uploadStrategyMap = new ConcurrentHashMap<>();

    public void init() {
        // Reflections reflections = new Reflections(PACKAGE_NAME);
        // Set<Class<?>> clzSet = reflections.getTypesAnnotatedWith(UploadMode.class);
        // if (CollectionUtils.isNotEmpty(clzSet)) {
        //     for (Class<?> clz : clzSet) {
        //         UploadMode uploadMode = clz.getAnnotation(UploadMode.class);
        //         uploadStrategyMap.put(uploadMode.mode(), (Class<SliceUploadStrategy>) clz);
        //     }
        // }
    }

    public SliceUploadStrategy getInstance(UploadModeEnum mode) {
        return this.getStrategyByType(mode);
    }

    private SliceUploadStrategy getStrategyByType(UploadModeEnum mode) {
        Class<SliceUploadStrategy> clz = uploadStrategyMap.get(mode);
        Assert.notNull(clz, "mode:" + mode + "can not found class,please checked");
        return SpringContextHolder.getBean(clz);
    }
}
