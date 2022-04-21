package com.taotao.cloud.stock.biz.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xtoon.cloud.sys.infrastructure.persistence.entity.SysPermissionDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 权限Mapper
 *
 * @author shuigedeng
 * @date 2021-02-14
 */
@Mapper
public interface SysPermissionMapper extends BaseMapper<SysPermissionDO> {

    /**
     * 查询权限
     *
     * @param params
     * @return
     */
    List<SysPermissionDO> queryList(@Param("params") Map<String, Object> params);


    /**
     * 查询角色的所有权限
     *
     * @param roleId 角色ID
     */
    List<SysPermissionDO> queryPermissionByRoleId(String roleId);

    /**
     * 查询管理员权限
     *
     * @return
     */
    List<SysPermissionDO> queryPermissionByRoleCode(String roleCode);

    /**
     * 查询用户权限
     *
     * @param userId
     * @return
     */
    List<SysPermissionDO> queryPermissionByUserId(String userId);

}
