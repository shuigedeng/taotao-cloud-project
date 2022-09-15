package com.taotao.cloud.stock.biz.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 用户角色关联Mapper
 *
 * @author shuigedeng
 * @date 2021-02-14
 */
@Mapper
public interface SysUserRoleMapper extends BaseSuperMapper<SysUserRoleDO> {

    /**
     * 根据角色ID，批量删除
     */
    int deleteByRoleIds(List<String> roleIds);

    /**
     * 根据用户ID，批量删除
     */
    int deleteByUserIds(List<String> userIds);
}
