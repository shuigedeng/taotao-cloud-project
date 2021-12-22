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
package com.taotao.cloud.common.tree;

import java.io.Serializable;
import java.util.List;

/**
 * 节点
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 22:21:57
 */
public interface INode extends Serializable {

	/**
	 * getId
	 *
	 * @return {@link java.lang.Long }
	 * @author shuigedeng
	 * @since 2021-09-02 22:22:11
	 */
	Long getId();

	/**
	 * getParentId
	 *
	 * @return {@link java.lang.Long }
	 * @author shuigedeng
	 * @since 2021-09-02 22:22:18
	 */
	Long getParentId();

	/**
	 * getChildren
	 *
	 * @return {@link java.util.List }
	 * @author shuigedeng
	 * @since 2021-09-02 22:22:22
	 */
	List<INode> getChildren();

	/**
	 * getHasChildren
	 *
	 * @return {@link java.lang.Boolean }
	 * @author shuigedeng
	 * @since 2021-09-02 22:22:30
	 */
	default Boolean getHasChildren() {
		return false;
	}
}
