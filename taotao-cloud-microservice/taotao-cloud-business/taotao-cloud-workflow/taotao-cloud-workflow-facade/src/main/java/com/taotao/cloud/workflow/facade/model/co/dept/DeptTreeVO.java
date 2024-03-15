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

package com.taotao.cloud.workflow.facade.model.co.dept;

import com.taotao.cloud.common.tree.INode;
import com.taotao.cloud.common.tree.MapperNode;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * DepartVO
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-12-22 20:59:37
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(description = "部门树VO")
public class DeptTreeVO extends MapperNode implements INode {

    @Serial
    private static final long serialVersionUID = -4132785717179910025L;

    /// **
    // * 主键ID
    // */
    // private Long id;

    /// **
    // * 父节点ID
    // */
    // private Long parentId;

    /// **
    // * 子孙节点
    // */
    // private List<INode> children;

    /// **
    // * 是否有子孙节点
    // */
    // private Boolean hasChildren;

    /** 部门名称 */
    private String name;
    /// **
    // * 排序
    // */
    // private Integer sort;
    /** 删除标识 */
    private String isDeleted;
    /** 租户ID */
    private Long tenantId;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "修改时间")
    private LocalDateTime updateTime;

    // @Override
    // public List<INode> getChildren() {
    //	if (this.children == null) {
    //		this.children = new ArrayList<>();
    //	}
    //	return this.children;
    // }

    @Override
    public <T extends INode> List<T> getChildren() {
        return super.getChildren();
    }
}
