package com.taotao.cloud.log.biz.log.core.mongo.service;

import cn.hutool.core.util.IdUtil;
import com.taotao.cloud.common.model.PageResult;
import com.taotao.cloud.common.utils.common.JsonUtils;
import com.taotao.cloud.common.utils.common.SecurityUtils;
import com.taotao.cloud.log.biz.log.core.mongo.dao.DataVersionLogMongoRepository;
import com.taotao.cloud.log.biz.log.core.mongo.entity.DataVersionLogMongo;
import com.taotao.cloud.log.biz.log.dto.DataVersionLogDto;
import com.taotao.cloud.log.biz.log.param.DataVersionLogParam;
import com.taotao.cloud.log.biz.log.service.DataVersionLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author shuigedeng
 * @date 2022/1/10
 */
@Slf4j
@Service
@ConditionalOnProperty(prefix = "log.store", value = "type", havingValue = "mongodb")
@RequiredArgsConstructor
public class DataVersionLogMongoService implements DataVersionLogService {
	private final DataVersionLogMongoRepository repository;
	private final MongoTemplate mongoTemplate;

	/**
	 * 添加
	 */
	@Override
	public void add(DataVersionLogParam param) {
		// 查询数据最新版本
		Criteria criteria = Criteria.where(DataVersionLogMongo.Fields.tableName).is(param.getDataName())
			.and(DataVersionLogMongo.Fields.dataId).is(param.getDataId());
		Sort sort = Sort.by(Sort.Order.desc(DataVersionLogMongo.Fields.version));
		Query query = new Query().addCriteria(criteria).with(sort).limit(1);
		DataVersionLogMongo one = mongoTemplate.findOne(query, DataVersionLogMongo.class);
		Integer maxVersion = Optional.ofNullable(one).map(DataVersionLogMongo::getVersion).orElse(0);

		DataVersionLogMongo dataVersionLog = new DataVersionLogMongo();
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
		dataVersionLog.setId(IdUtil.getSnowflakeNextId());
		repository.save(dataVersionLog);
	}

	@Override
	public DataVersionLogDto findById(Long id) {
		return repository.findById(id).map(DataVersionLogMongo::toDto).orElseThrow(RuntimeException::new);
	}

	@Override
	public PageResult<DataVersionLogDto> page(DataVersionLogParam param) {
		DataVersionLogMongo dataVersionLogMongo = new DataVersionLogMongo()
			.setDataId(param.getDataId())
			.setVersion(param.getVersion())
			.setTableName(param.getTableName())
			.setDataName(param.getDataName());
		// 查询条件
		ExampleMatcher matching = ExampleMatcher.matching()
			.withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
		Example<DataVersionLogMongo> example = Example.of(dataVersionLogMongo, matching);
		//设置分页条件 (第几页，每页大小，排序)
		Sort sort = Sort.by(Sort.Order.desc("id"));
		Pageable pageable = PageRequest.of(param.getCurrentPage() - 1, param.getPageSize(), sort);

		Page<DataVersionLogMongo> page = repository.findAll(example, pageable);
		List<DataVersionLogDto> records = page.getContent().stream()
			.map(DataVersionLogMongo::toDto)
			.collect(Collectors.toList());

		return PageResult.of(page.getTotalElements(), 1, param.getCurrentPage(), param.getPageSize(), records);

	}

	@Override
	public void delete(Long id) {

	}
}
