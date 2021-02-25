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
package com.taotao.cloud.front.graphql.entity;

import lombok.Data;

/**
 * @author dengtao
 * @since 2020/11/9 10:56
 * @version 1.0.0
 */
@Data
public class Article {
	private String id;
	private User author;
	private String title;
	private String content;
	private String createBy;
	private Integer thumbUp;
}
