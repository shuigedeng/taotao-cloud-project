package com.taotao.cloud.log.biz.log.core.db.service;

import com.taotao.cloud.common.model.PageResult;
import com.taotao.cloud.data.mybatisplus.pagehelper.PageParam;
import com.taotao.cloud.log.biz.log.core.db.convert.LogConvert;
import com.taotao.cloud.log.biz.log.core.db.dao.OperateLogDbManager;
import com.taotao.cloud.log.biz.log.core.db.entity.OperateLogDb;
import com.taotao.cloud.log.biz.log.dto.OperateLogDto;
import com.taotao.cloud.log.biz.log.param.OperateLogParam;
import com.taotao.cloud.log.biz.log.service.OperateLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/**
 * 操作日志
 *
 * @author shuigedeng
 * @date 2021/8/12
 */
@Slf4j
@Service
@ConditionalOnProperty(prefix = "log.store", value = "type", havingValue = "jdbc", matchIfMissing = true)
@RequiredArgsConstructor
public class OperateLogDbService implements OperateLogService {
	private final OperateLogDbManager operateLogManager;

	/**
	 * 添加
	 */
	@Override
	public void add(OperateLogParam operateLog) {
		operateLogManager.save(LogConvert.CONVERT.convert(operateLog));
	}

	/**
	 * 获取
	 */
	@Override
	public OperateLogDto findById(Long id) {
		return operateLogManager.findById(id).map(OperateLogDb::toDto).orElseThrow(RuntimeException::new);
	}

	/**
	 * 分页
	 */
	@Override
	public PageResult<OperateLogDto> page( OperateLogParam operateLogParam) {
		return PageResult.convertMybatisPage(operateLogManager.page(operateLogParam), OperateLogDto.class);
	}

	/**
	 * 删除
	 */
	@Override
	public void delete(Long id) {
		operateLogManager.deleteById(id);
	}
}
