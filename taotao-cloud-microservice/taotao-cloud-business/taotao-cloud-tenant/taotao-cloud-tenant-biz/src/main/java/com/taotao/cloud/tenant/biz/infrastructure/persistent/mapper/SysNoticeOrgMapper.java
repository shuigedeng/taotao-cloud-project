package com.taotao.cloud.tenant.biz.infrastructure.persistent.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taotao.cloud.tenant.biz.domain.aggregate.SysNoticeOrg;
import org.apache.ibatis.annotations.Mapper;

/**
 * 通知公告组织关联Mapper接口
 */
@Mapper
public interface SysNoticeOrgMapper extends BaseMapper<SysNoticeOrg> {

}
