/*
 * Copyright (c) 2020 taotao cloud Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*
 *
 * 此类来自 https://gitee.com/geek_qi/cloud-platform/blob/master/ace-common/src/main/java/com/github/wxiaoqi/security/common/vo/TreeNode.java
 * @ Apache-2.0
 */

package com.taotao.cloud.uc.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

/**
 * TreeNode
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2020/10/21 11:02
 */
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Schema(name = "TreeNode", description = "树节点")
public class TreeNode implements Serializable {

	private static final long serialVersionUID = -4546704465269983480L;

	@Schema(description = "id")
	protected Long id;

	@Schema(description = "parentId")
	protected Long parentId;

	@Schema(description = "children")
	protected List<TreeNode> children;

	@Schema(description = "是否包含子节点")
	@Builder.Default
	private Boolean hasChildren = false;

	public void add(TreeNode node) {
		children.add(node);
	}

}
