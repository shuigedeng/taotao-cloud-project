package com.taotao.cloud.tenant.biz.infrastructure.persistent.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taotao.cloud.tenant.biz.domain.aggregate.SysNotice;
import org.apache.ibatis.annotations.Mapper;

/**
 * 通知公告Mapper接口
 */
@Mapper
public interface SysNoticeMapper extends BaseMapper<SysNotice> {

}
