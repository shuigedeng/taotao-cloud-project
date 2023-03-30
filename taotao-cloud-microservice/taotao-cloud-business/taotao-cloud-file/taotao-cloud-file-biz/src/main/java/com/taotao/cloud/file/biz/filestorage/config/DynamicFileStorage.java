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

package com.taotao.cloud.file.biz.filestorage.config;

import com.taotao.cloud.oss.common.storage.platform.LocalFileStorage;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** 动态存储平台设置 */
@Component
public class DynamicFileStorage {

    @Autowired private List<LocalFileStorage> list;

    public void add() {
        // TODO 读取数据库配置
        LocalFileStorage localFileStorage = new LocalFileStorage();
        localFileStorage.setPlatform("my-local-1"); // 平台名称
        localFileStorage.setBasePath("");
        localFileStorage.setDomain("");
        list.add(localFileStorage);
    }

    public void remove(String platform) {
        for (LocalFileStorage localFileStorage : list) {}
    }
}
