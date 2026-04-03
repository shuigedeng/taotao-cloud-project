package com.taotao.cloud.tenant.biz.infrastructure.persistent.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taotao.cloud.tenant.biz.domain.aggregate.SysPost;
import org.apache.ibatis.annotations.Mapper;

/**
 * 岗位Mapper接口
 */
@Mapper
public interface SysPostMapper extends BaseMapper<SysPost> {

}
