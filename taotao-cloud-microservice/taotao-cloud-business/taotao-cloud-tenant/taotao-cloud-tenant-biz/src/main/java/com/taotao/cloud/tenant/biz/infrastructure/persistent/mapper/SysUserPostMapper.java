package com.taotao.cloud.tenant.biz.infrastructure.persistent.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taotao.cloud.tenant.biz.domain.aggregate.SysUserPost;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户-岗位关联Mapper接口
 */
@Mapper
public interface SysUserPostMapper extends BaseMapper<SysUserPost> {

}
