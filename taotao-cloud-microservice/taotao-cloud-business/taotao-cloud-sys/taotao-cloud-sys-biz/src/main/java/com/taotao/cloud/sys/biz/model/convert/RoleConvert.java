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

package com.taotao.cloud.sys.biz.model.convert;

import com.taotao.cloud.sys.biz.model.dto.role.RoleQueryDTO;
import com.taotao.cloud.sys.biz.model.vo.role.RoleQueryVO;
import com.taotao.cloud.sys.biz.model.bo.RoleBO;
import com.taotao.cloud.sys.biz.model.entity.system.Role;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * UserMapper
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 13:39:49
 */
@Mapper(unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoleConvert {

    /** 实例 */
    RoleConvert INSTANCE = Mappers.getMapper(RoleConvert.class);

    /**
     * bos, vos
     *
     * @param bos bos
     * @return {@link List }<{@link RoleQueryVO }>
     * @since 2022-04-28 13:39:50
     */
    List<RoleQueryVO> convertListVO(List<RoleBO> bos);

    /**
     * 博给签证官
     *
     * @param bo 薄
     * @return {@link RoleQueryVO }
     * @since 2022-04-28 13:39:50
     */
    RoleQueryVO convert(RoleBO bo);

    /**
     * 薄角色
     *
     * @param role 角色
     * @return {@link RoleBO }
     * @since 2022-04-28 13:39:50
     */
    RoleBO convertBO(Role role);

    /**
     * 角色bos
     *
     * @param roles 角色
     * @return {@link List }<{@link RoleBO }>
     * @since 2022-04-28 13:39:50
     */
    List<RoleBO> convertListBO(List<Role> roles);

    /**
     * 系统用户用户查询签证官
     *
     * @param sysRole 系统作用
     * @return {@link RoleQueryVO }
     * @since 2022-04-28 13:39:50
     */
    RoleQueryVO convertVO(Role sysRole);

    /**
     * 系统用户用户查询签证官
     *
     * @param roleList 角色列表
     * @return {@link List }<{@link RoleQueryVO }>
     * @since 2022-04-28 13:39:50
     */
    List<RoleQueryVO> convertListQueryVO(List<Role> roleList);

    /**
     * 用户dto复制到系统用户
     *
     * @param roleQueryDTO 角色查询dto
     * @param sysRole 系统作用
     * @since 2022-04-28 13:39:51
     */
    void copy(RoleQueryDTO roleQueryDTO, @MappingTarget Role sysRole);
}
