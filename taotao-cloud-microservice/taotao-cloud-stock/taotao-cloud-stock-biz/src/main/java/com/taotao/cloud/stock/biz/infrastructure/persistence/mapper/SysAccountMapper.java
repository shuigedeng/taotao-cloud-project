package com.taotao.cloud.stock.biz.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xtoon.cloud.sys.infrastructure.persistence.entity.SysAccountDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 账号Mapper
 *
 * @author shuigedeng
 * @date 2021-02-10
 **/
@Mapper
public interface SysAccountMapper extends BaseMapper<SysAccountDO> {
}
