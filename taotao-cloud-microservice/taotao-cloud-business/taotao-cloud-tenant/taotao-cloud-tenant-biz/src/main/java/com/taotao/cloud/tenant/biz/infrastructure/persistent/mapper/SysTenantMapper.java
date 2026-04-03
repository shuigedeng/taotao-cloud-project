package com.taotao.cloud.tenant.biz.infrastructure.persistent.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taotao.cloud.tenant.biz.domain.aggregate.SysTenant;
import org.apache.ibatis.annotations.Mapper;

/**
 * 租户Mapper接口
 */
@Mapper
public interface SysTenantMapper extends BaseMapper<SysTenant> {

}
