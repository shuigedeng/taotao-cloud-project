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

import com.taotao.cloud.job.worker.processor.type.BasicProcessor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 处理器对象
 *
 * @author shuigedeng
 * @since 2022/9/23
 */
@Getter
@Setter
@Accessors(chain = true)
public class ProcessorBean {

    /**
     * 真正用来执行逻辑的处理器对象
     */
    private transient BasicProcessor processor;

    /**
     * 加载该处理器对象的 classLoader，可空，空则使用 {@link Object#getClass()#getClassLoader() 代替}
     */
    private transient ClassLoader classLoader;

    /**
     * Bean 是否稳定
     * SpringBean / 普通Java 对象，在整个 JVM 生命周期内都不会变，可声明为稳定，在上层缓存，避免每次都要重现 build processor
     * 对于动态容器，可能在部署后改变，则需要声明为不稳定
     */
    private boolean stable = true;
}
