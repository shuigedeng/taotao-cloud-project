package com.taotao.cloud.log.biz.log.core.db.dao;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.data.mybatisplus.utils.BaseManager;
import com.taotao.cloud.log.biz.log.core.db.entity.DataVersionLogDb;
import com.taotao.cloud.log.biz.log.param.DataVersionLogParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Objects;

/**
 * @author shuigedeng
 * @date 2022/1/10
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class DataVersionLogDbManager extends BaseManager<DataVersionLogDbMapper, DataVersionLogDb> {
	private final DataVersionLogDbMapper mapper;

	/**
	 * 获取最新版本号
	 */
	public int getMaxVersion(String dataName, String dataId) {
		Integer maxVersion = mapper.getMaxVersion(dataName, dataId);
		return Objects.isNull(maxVersion) ? 0 : maxVersion;
	}

	/**
	 * 分页
	 *
	 * @param pageParam
	 * @param param
	 * @return
	 */
	public IPage<DataVersionLogDb> page(DataVersionLogParam param) {
		return lambdaQuery()
			.orderByDesc(DataVersionLogDb::getId)
			.like(StrUtil.isNotBlank(param.getDataName()), DataVersionLogDb::getDataName, param.getDataName())
			.like(StrUtil.isNotBlank(param.getTableName()), DataVersionLogDb::getTableName, param.getTableName())
			.like(StrUtil.isNotBlank(param.getDataId()), DataVersionLogDb::getDataId, param.getDataId())
			.eq(Objects.nonNull(param.getVersion()), DataVersionLogDb::getVersion, param.getVersion())
			.page(param.buildMpPage());
	}
}
