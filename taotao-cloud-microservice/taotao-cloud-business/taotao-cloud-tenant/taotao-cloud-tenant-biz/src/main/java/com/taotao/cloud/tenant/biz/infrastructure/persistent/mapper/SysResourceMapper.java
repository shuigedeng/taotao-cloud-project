package com.taotao.cloud.tenant.biz.infrastructure.persistent.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taotao.cloud.tenant.biz.domain.aggregate.SysResource;
import org.apache.ibatis.annotations.Mapper;

/**
 * 资源Mapper接口
 */
@Mapper
public interface SysResourceMapper extends BaseMapper<SysResource> {

}
