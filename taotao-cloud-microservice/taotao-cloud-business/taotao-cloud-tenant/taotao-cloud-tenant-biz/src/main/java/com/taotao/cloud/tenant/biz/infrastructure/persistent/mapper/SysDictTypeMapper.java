package com.taotao.cloud.tenant.biz.infrastructure.persistent.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taotao.cloud.tenant.biz.domain.aggregate.SysDictType;
import org.apache.ibatis.annotations.Mapper;

/**
 * 字典类型Mapper接口
 */
@Mapper
public interface SysDictTypeMapper extends BaseMapper<SysDictType> {

}
