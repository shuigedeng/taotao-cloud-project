package com.taotao.cloud.tenant.biz.infrastructure.persistent.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taotao.cloud.tenant.biz.domain.aggregate.SysConfig;
import org.apache.ibatis.annotations.Mapper;

/**
 * 系统配置Mapper接口
 */
@Mapper
public interface SysConfigMapper extends BaseMapper<SysConfig> {

}
