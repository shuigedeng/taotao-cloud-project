/*
 * Copyright 2002-2021 the original author or authors.
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
package com.taotao.cloud.data.mybatis.plus.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * mybatis-plus 自动填充Properties
 *
 * @author dengtao
 * @since 2020/5/2 11:19
 * @version 1.0.0
 */
@Data
@RefreshScope
@ConfigurationProperties(prefix = "taotao.cloud.data.mybatis-plus.auto-fill")
public class MybatisPlusAutoFillProperties {
    /**
     * 是否开启自动填充字段
     */
    private Boolean enabled = false;
    /**
     * 是否开启了插入填充
     */
    private Boolean enableInsertFill = true;
    /**
     * 是否开启了更新填充
     */
    private Boolean enableUpdateFill = true;
    /**
     * 创建时间字段名
     */
    private String createTimeField = "createTime";
    /**
     * 更新时间字段名
     */
    private String updateTimeField = "updateTime";
}
