package com.taotao.cloud.tenant.biz.infrastructure.persistent.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taotao.cloud.tenant.biz.domain.aggregate.SysUserOrg;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户-组织关联Mapper接口
 */
@Mapper
public interface SysUserOrgMapper extends BaseMapper<SysUserOrg> {

}
