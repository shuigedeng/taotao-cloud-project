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

package com.taotao.cloud.sys.biz.service.business;

import com.taotao.cloud.sys.api.dubbo.request.DubboMenuQueryRequest;
import com.taotao.cloud.sys.biz.model.vo.menu.MenuQueryVO;
import com.taotao.cloud.sys.biz.model.vo.menu.MenuTreeVO;
import com.taotao.cloud.sys.biz.model.bo.MenuBO;
import com.taotao.cloud.sys.biz.model.entity.system.Resource;
import com.taotao.cloud.web.base.service.BaseSuperService;
import java.util.List;
import java.util.Set;

/**
 * ISysMenuService
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-10-09 20:38:19
 */
public interface IResourceService extends BaseSuperService<Resource, Long> {

    /**
     * 查询所有菜单列表
     *
     * @return 菜单列表
     * @since 2021-10-09 20:39:01
     */
    List<MenuBO> findAllMenus();

    /**
     * 根据id查询菜单列表
     *
     * @param id id
     * @return 菜单列表
     * @since 2022-03-23 08:55:48
     */
    List<DubboMenuQueryRequest> findAllById(Long id);

    /**
     * 根据角色id列表获取角色列表
     *
     * @param roleIds 角色id列表
     * @return 角色列表
     * @since 2021-10-09 20:39:07
     */
    List<MenuBO> findMenuByRoleIds(Set<Long> roleIds);

    /**
     * 根据角色cde列表获取角色列表
     *
     * @param codes 角色cde列表
     * @return 角色列表
     * @since 2021-10-09 20:39:14
     */
    List<MenuBO> findMenuByCodes(Set<String> codes);

    /**
     * 根据parentId获取角色列表
     *
     * @param parentId fuid
     * @return 角色列表
     * @since 2021-10-09 20:39:19
     */
    List<MenuBO> findMenuByParentId(Long parentId);

    /**
     * 根据id列表查询菜单信息
     *
     * @param idList id列表
     * @return 菜单信息
     * @since 2021-10-09 20:39:48
     */
    List<MenuBO> findMenuByIdList(List<Long> idList);

    /**
     * 获取树形菜单集合 1.false-非懒加载，查询全部 " + "2.true-懒加载，根据parentId查询 2.1 父节点为空，则查询parentId=0
     *
     * @param lazy 是否懒加载
     * @param parentId 父id
     * @return 树形菜单集合
     * @since 2021-10-09 20:39:29
     */
    List<MenuTreeVO> findMenuTree(boolean lazy, Long parentId);

    /**
     * 获取当前用户树形菜单列表
     *
     * @param MenuVOList 查询参数
     * @param parentId 父id
     * @return 树形菜单列表
     * @since 2021-10-09 20:39:41
     */
    List<MenuTreeVO> findCurrentUserMenuTree(List<MenuQueryVO> MenuVOList, Long parentId);

    // Future<Boolean> testAsync();
    //
    // Boolean testSeata();
}
