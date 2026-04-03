package com.taotao.cloud.tenant.biz.infrastructure.persistent.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taotao.cloud.tenant.biz.domain.aggregate.SysExcelExportConfig;
import org.apache.ibatis.annotations.Mapper;

/**
 * Excel导出配置Mapper
 */
@Mapper
public interface SysExcelExportConfigMapper extends BaseMapper<SysExcelExportConfig> {
}
