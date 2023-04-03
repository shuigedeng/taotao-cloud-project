package com.taotao.cloud.log.biz.log.core.mongo.service;

import cn.hutool.core.util.IdUtil;
import com.taotao.cloud.common.model.PageResult;
import com.taotao.cloud.log.biz.log.core.mongo.convert.LogConvert;
import com.taotao.cloud.log.biz.log.core.mongo.dao.OperateLogMongoRepository;
import com.taotao.cloud.log.biz.log.core.mongo.entity.OperateLogMongo;
import com.taotao.cloud.log.biz.log.dto.OperateLogDto;
import com.taotao.cloud.log.biz.log.param.OperateLogParam;
import com.taotao.cloud.log.biz.log.service.OperateLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * MongoDB存储实现
 *
 * @author shuigedeng
 * @date 2021/12/2
 */
@Slf4j
@Service
@ConditionalOnProperty(prefix = "log.store", value = "type", havingValue = "mongodb")
@RequiredArgsConstructor
public class OperateLogMongoService implements OperateLogService {
	private final OperateLogMongoRepository repository;

	@Override
	public void add(OperateLogParam operateLog) {
		OperateLogMongo operateLogMongo = LogConvert.CONVERT.convert(operateLog);
		operateLogMongo.setId(IdUtil.getSnowflakeNextId());
		repository.save(operateLogMongo);
	}

	@Override
	public OperateLogDto findById(Long id) {
		return repository.findById(id).map(OperateLogMongo::toDto).orElseThrow(RuntimeException::new);
	}

	@Override
	public PageResult<OperateLogDto> page(OperateLogParam operateLogParam) {
		// 查询条件
		ExampleMatcher matching = ExampleMatcher.matching()
			.withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
		Example<OperateLogMongo> example = Example.of(LogConvert.CONVERT.convert(operateLogParam), matching);

		//设置分页条件 (第几页，每页大小，排序)
		Sort sort = Sort.by(Sort.Order.desc("id"));
		Pageable pageable = PageRequest.of(operateLogParam.getCurrentPage() - 1, operateLogParam.getPageSize(), sort);

		Page<OperateLogMongo> page = repository.findAll(example, pageable);
		List<OperateLogDto> records = page.getContent().stream()
			.map(OperateLogMongo::toDto)
			.collect(Collectors.toList());

		return PageResult.of(page.getTotalElements(), 1, operateLogParam.getCurrentPage(), operateLogParam.getPageSize(), records);
	}

	@Override
	public void delete(Long id) {
		repository.deleteById(id);
	}
}
