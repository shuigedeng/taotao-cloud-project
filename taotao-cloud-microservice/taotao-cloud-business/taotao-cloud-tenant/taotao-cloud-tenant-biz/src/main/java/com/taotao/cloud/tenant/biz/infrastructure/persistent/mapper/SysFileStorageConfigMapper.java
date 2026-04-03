package com.taotao.cloud.tenant.biz.infrastructure.persistent.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taotao.cloud.tenant.biz.domain.aggregate.SysFileStorageConfig;
import org.apache.ibatis.annotations.Mapper;

/**
 * 文件存储配置Mapper
 */
@Mapper
public interface SysFileStorageConfigMapper extends BaseMapper<SysFileStorageConfig> {
}
