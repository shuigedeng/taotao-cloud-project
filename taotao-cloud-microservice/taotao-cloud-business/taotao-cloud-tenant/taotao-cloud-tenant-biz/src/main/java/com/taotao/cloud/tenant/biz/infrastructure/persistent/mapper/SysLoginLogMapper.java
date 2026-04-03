package com.taotao.cloud.tenant.biz.infrastructure.persistent.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taotao.cloud.tenant.biz.domain.aggregate.SysLoginLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * 登录日志Mapper
 */
@Mapper
public interface SysLoginLogMapper extends BaseMapper<SysLoginLog> {

}
