package com.taotao.cloud.stock.biz.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * 租户Mapper
 *
 * @author shuigedeng
 * @date 2021-02-14
 */
@Mapper
public interface SysTenantMapper extends BaseSuperMapper<SysTenantDO> {

	/**
	 * 分页查询租户
	 *
	 * @param params
	 * @return
	 */
	IPage<SysTenantDO> queryPage(IPage<SysTenantDO> page, @Param("params") Map<String, Object> params);
}
