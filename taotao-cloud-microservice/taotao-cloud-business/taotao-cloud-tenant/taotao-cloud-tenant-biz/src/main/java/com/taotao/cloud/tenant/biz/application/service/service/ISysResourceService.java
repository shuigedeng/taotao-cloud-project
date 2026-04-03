package com.taotao.cloud.tenant.biz.application.service.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.tenant.biz.application.dto.SysResourceDTO;
import com.taotao.cloud.tenant.biz.application.dto.SysResourceQuery;
import com.taotao.cloud.tenant.biz.domain.aggregate.SysResource;
import com.mdframe.forge.plugin.system.vo.UserResourceTreeVO;

import java.util.List;

/**
 * 资源Service接口
 */
public interface ISysResourceService extends IService<SysResource> {

    /**
     * 分页查询资源列表
     *
     * @param query 查询条件
     * @return 资源分页列表
     */
    IPage<SysResource> selectResourcePage(SysResourceQuery query);

    /**
     * 查询资源树形列表
     *
     * @param query 查询条件
     * @return 资源列表
     */
    List<SysResource> selectResourceTree(SysResourceQuery query);

    /**
     * 根据ID查询资源详情
     *
     * @param id 资源ID
     * @return 资源详情
     */
    SysResource selectResourceById(Long id);

    /**
     * 新增资源
     *
     * @param dto 资源信息
     * @return 是否成功
     */
    boolean insertResource(SysResourceDTO dto);

    /**
     * 修改资源
     *
     * @param dto 资源信息
     * @return 是否成功
     */
    boolean updateResource(SysResourceDTO dto);

    /**
     * 删除资源
     *
     * @param id 资源ID
     * @return 是否成功
     */
    boolean deleteResourceById(Long id);

    /**
     * 查询当前用户的资源树（包含菜单和按钮权限）
     *
     * @return 用户资源树列表
     */
    List<UserResourceTreeVO> selectCurrentUserResourceTree();

    /**
     * 查询当前用户的菜单树（仅包含目录和菜单，不包含按钮）
     *
     * @return 用户菜单树列表
     */
    List<UserResourceTreeVO> selectCurrentUserMenuTree();

    /**
     * 查询当前用户的权限标识列表（按钮权限）
     *
     * @return 权限标识列表
     */
    List<String> selectCurrentUserPermissions();

    /**
     * 查询当前用户的资源ID列表
     *
     * @return 资源ID列表
     */
    List<Long> selectCurrentUserResourceIds();
}
