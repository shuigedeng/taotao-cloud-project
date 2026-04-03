package com.taotao.cloud.tenant.biz.infrastructure.persistent.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taotao.cloud.tenant.biz.domain.aggregate.SysRoleResource;
import org.apache.ibatis.annotations.Mapper;

/**
 * 角色-资源关联Mapper接口
 */
@Mapper
public interface SysRoleResourceMapper extends BaseMapper<SysRoleResource> {

}
