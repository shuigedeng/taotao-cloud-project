package com.taotao.cloud.tenant.biz.infrastructure.persistent.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taotao.cloud.tenant.biz.domain.aggregate.SysUserRole;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户-角色关联Mapper接口
 */
@Mapper
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {

}
