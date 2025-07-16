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

package com.taotao.cloud.mq.consistency.raft1.server.core;

import com.taotao.cloud.mq.consistency.raft1.common.entity.req.dto.LogEntry;

/**
 * 日志模块
 *
 * @since 1.0.0
 */
public interface LogManager {

    /**
     * 写入
     * @param logEntry 日志
     */
    void write(LogEntry logEntry);

    /**
     * 读取
     * @param index 下标志
     * @return 结果
     */
    LogEntry read(Long index);

    /**
     * 从开始位置删除
     * @param startIndex 开始位置
     */
    void removeOnStartIndex(Long startIndex);

    /**
     * 获取最新的日志
     * @return 日志
     */
    LogEntry getLast();

    /**
     * 获取最新的下标
     * @return 结果
     */
    Long getLastIndex();
}
