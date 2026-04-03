package com.taotao.cloud.tenant.biz.infrastructure.persistent.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taotao.cloud.tenant.biz.domain.aggregate.SysExcelColumnConfig;
import org.apache.ibatis.annotations.Mapper;

/**
 * Excel列配置Mapper
 */
@Mapper
public interface SysExcelColumnConfigMapper extends BaseMapper<SysExcelColumnConfig> {
}
