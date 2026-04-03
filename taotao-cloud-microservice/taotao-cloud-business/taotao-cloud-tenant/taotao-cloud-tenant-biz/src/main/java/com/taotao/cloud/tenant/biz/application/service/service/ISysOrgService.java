package com.taotao.cloud.tenant.biz.application.service.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.tenant.biz.application.dto.SysOrgDTO;
import com.taotao.cloud.tenant.biz.application.dto.SysOrgQuery;
import com.taotao.cloud.tenant.biz.domain.aggregate.SysOrg;

import java.util.List;

/**
 * 组织Service接口
 */
public interface ISysOrgService extends IService<SysOrg> {

    /**
     * 分页查询组织列表
     *
     * @param query 查询条件
     * @return 组织分页列表
     */
    IPage<SysOrg> selectOrgPage(SysOrgQuery query);

    /**
     * 查询组织树形列表
     *
     * @param query 查询条件
     * @return 组织列表
     */
    List<SysOrg> selectOrgTree(SysOrgQuery query);

    /**
     * 根据ID查询组织详情
     *
     * @param id 组织ID
     * @return 组织详情
     */
    SysOrg selectOrgById(Long id);

    /**
     * 新增组织
     *
     * @param dto 组织信息
     * @return 是否成功
     */
    boolean insertOrg(SysOrgDTO dto);

    /**
     * 修改组织
     *
     * @param dto 组织信息
     * @return 是否成功
     */
    boolean updateOrg(SysOrgDTO dto);

    /**
     * 删除组织
     *
     * @param id 组织ID
     * @return 是否成功
     */
    boolean deleteOrgById(Long id);
}
