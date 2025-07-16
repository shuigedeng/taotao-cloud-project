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

package com.taotao.cloud.gateway.service;

import com.taotao.cloud.gateway.model.AccessRecord;
import java.util.HashSet;
import org.springframework.stereotype.Service;

/**
 * 访问日志Service类
 */
@Service
public class VisitLogService {

    // todo 需要实现
    public boolean saveBatch(HashSet<AccessRecord> oldCache, int batchSize) {
        return false;
    }
}
