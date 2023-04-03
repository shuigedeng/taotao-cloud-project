package com.taotao.cloud.log.biz.log.core.db.service;

import com.taotao.cloud.common.model.PageResult;
import com.taotao.cloud.common.utils.common.JsonUtils;
import com.taotao.cloud.common.utils.common.SecurityUtils;
import com.taotao.cloud.log.biz.log.core.db.dao.DataVersionLogDbManager;
import com.taotao.cloud.log.biz.log.core.db.entity.DataVersionLogDb;
import com.taotao.cloud.log.biz.log.dto.DataVersionLogDto;
import com.taotao.cloud.log.biz.log.param.DataVersionLogParam;
import com.taotao.cloud.log.biz.log.service.DataVersionLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 数据版本日志数据库实现
 *
 * @author shuigedeng
 * @date 2022/1/10
 */
@Slf4j
@Service
@ConditionalOnProperty(prefix = "log.store", value = "type", havingValue = "jdbc", matchIfMissing = true)
@RequiredArgsConstructor
public class DataVersionLogDbService implements DataVersionLogService {
	private final DataVersionLogDbManager manager;

	/**
	 * 添加
	 */
	@Override
	@Transactional
	public void add(DataVersionLogParam param) {
		int maxVersion = manager.getMaxVersion(param.getTableName(), param.getDataId());
		DataVersionLogDb dataVersionLog = new DataVersionLogDb();
		dataVersionLog.setTableName(param.getTableName());
		dataVersionLog.setDataName(param.getDataName());
		dataVersionLog.setDataId(param.getDataId());
		dataVersionLog.setCreator(SecurityUtils.getUserIdWithAnonymous());
		dataVersionLog.setCreateTime(LocalDateTime.now());
		dataVersionLog.setVersion(maxVersion + 1);

		if (param.getDataContent() instanceof String) {
			dataVersionLog.setDataContent((String) param.getDataContent());
		} else {
			dataVersionLog.setDataContent(JsonUtils.toJson(param.getDataContent()));
		}
		if (param.getChangeContent() instanceof String) {
			dataVersionLog.setChangeContent(param.getChangeContent());
		} else {
			if (Objects.nonNull(param.getChangeContent())) {
				dataVersionLog.setChangeContent(JsonUtils.toJson(param.getChangeContent()));
			}
		}
		manager.save(dataVersionLog);
	}

	/**
	 * 获取
	 */
	@Override
	public DataVersionLogDto findById(Long id) {
		return manager.findById(id).map(DataVersionLogDb::toDto).orElseThrow(RuntimeException::new);
	}

	/**
	 * 分页
	 */
	@Override
	public PageResult<DataVersionLogDto> page(DataVersionLogParam param) {
		return PageResult.convertMybatisPage(manager.page( param), DataVersionLogDto.class);
	}

	/**
	 * 删除
	 */
	@Override
	public void delete(Long id) {

	}
}
