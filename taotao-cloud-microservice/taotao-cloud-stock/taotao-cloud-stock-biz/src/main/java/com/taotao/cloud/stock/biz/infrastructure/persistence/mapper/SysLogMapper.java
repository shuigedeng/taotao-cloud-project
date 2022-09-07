package com.taotao.cloud.stock.biz.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xtoon.cloud.sys.infrastructure.persistence.entity.SysLogDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 日志Mapper
 *
 * @author shuigedeng
 * @date 2021-01-23
 */
@Mapper
public interface SysLogMapper extends BaseSuperMapper<SysLogDO> {

}
