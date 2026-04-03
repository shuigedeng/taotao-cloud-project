package com.taotao.cloud.tenant.biz.infrastructure.persistent.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taotao.cloud.tenant.biz.domain.aggregate.SysOrg;
import org.apache.ibatis.annotations.Mapper;

/**
 * 组织Mapper接口
 */
@Mapper
public interface SysOrgMapper extends BaseMapper<SysOrg> {

}
