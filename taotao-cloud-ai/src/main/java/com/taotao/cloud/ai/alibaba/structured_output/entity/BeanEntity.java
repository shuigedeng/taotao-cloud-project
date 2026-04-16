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

package com.taotao.cloud.ai.alibaba.structured_output.entity;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * @author yingzi
 * @date 2025/5/22 22:17
 */
@JsonPropertyOrder({"title", "date", "author", "content"}) // 指定属性的顺序
public record BeanEntity(String title, String author, String date, String content) {}
