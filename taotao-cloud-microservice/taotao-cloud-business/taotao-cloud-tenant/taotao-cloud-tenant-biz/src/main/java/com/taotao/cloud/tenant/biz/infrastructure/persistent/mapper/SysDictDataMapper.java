package com.taotao.cloud.tenant.biz.infrastructure.persistent.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taotao.cloud.tenant.biz.domain.aggregate.SysDictData;
import org.apache.ibatis.annotations.Mapper;

/**
 * 字典数据Mapper接口
 */
@Mapper
public interface SysDictDataMapper extends BaseMapper<SysDictData> {

}
