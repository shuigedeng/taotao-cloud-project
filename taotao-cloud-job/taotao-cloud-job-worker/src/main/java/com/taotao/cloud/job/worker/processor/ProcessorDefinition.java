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

package com.taotao.cloud.job.worker.processor;

import java.io.Serializable;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 处理器定义
 * 对外暴露的对象尽量不要直接使用构造器等不方便后续扩展的 API，Getter & Setter 保兼容
 *
 * @author shuigedeng
 * @since 2023/1/17
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class ProcessorDefinition implements Serializable {

    /**
     * 后台配置的处理器类型
     */
    private String processorType;

    /**
     * 后台配置的处理器信息
     */
    private String processorInfo;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProcessorDefinition that = (ProcessorDefinition) o;
        return Objects.equals(processorType, that.processorType)
                && Objects.equals(processorInfo, that.processorInfo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(processorType, processorInfo);
    }
}
