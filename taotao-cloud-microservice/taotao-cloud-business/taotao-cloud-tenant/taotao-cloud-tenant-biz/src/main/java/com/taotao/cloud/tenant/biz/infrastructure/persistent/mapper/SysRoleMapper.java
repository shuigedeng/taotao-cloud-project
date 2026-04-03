package com.taotao.cloud.tenant.biz.infrastructure.persistent.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taotao.cloud.tenant.biz.domain.aggregate.SysRole;
import org.apache.ibatis.annotations.Mapper;

/**
 * 角色Mapper接口
 */
@Mapper
public interface SysRoleMapper extends BaseMapper<SysRole> {

}
