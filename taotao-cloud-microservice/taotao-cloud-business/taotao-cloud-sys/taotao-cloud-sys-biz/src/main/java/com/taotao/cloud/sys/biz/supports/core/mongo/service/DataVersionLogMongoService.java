/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.sys.biz.supports.core.mongo.service;

import com.taotao.boot.common.model.result.PageResult;
import com.taotao.boot.common.utils.common.JsonUtils;
import com.taotao.boot.security.spring.utils.SecurityUtils;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

/**
 * @author shuigedeng
 * @since 2022/1/10
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
        Criteria criteria = Criteria.where(DataVersionLogMongo.Fields.tableName)
                .is(param.getDataName())
                .and(DataVersionLogMongo.Fields.dataId)
                .is(param.getDataId());
        Sort sort = Sort.by(Sort.Order.desc(DataVersionLogMongo.Fields.version));
        Query query = new Query().addCriteria(criteria).with(sort).limit(1);
        DataVersionLogMongo one = mongoTemplate.findOne(query, DataVersionLogMongo.class);
        Integer maxVersion =
                Optional.ofNullable(one).map(DataVersionLogMongo::getVersion).orElse(0);

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
        ExampleMatcher matching = ExampleMatcher.matching().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example<DataVersionLogMongo> example = Example.of(dataVersionLogMongo, matching);
        // 设置分页条件 (第几页，每页大小，排序)
        Sort sort = Sort.by(Sort.Order.desc("id"));
        Pageable pageable = PageRequest.of(param.getCurrentPage() - 1, param.getPageSize(), sort);

        Page<DataVersionLogMongo> page = repository.findAll(example, pageable);
        List<DataVersionLogDto> records =
                page.getContent().stream().map(DataVersionLogMongo::toDto).toList();

        return PageResult.of(page.getTotalElements(), 1, param.getCurrentPage(), param.getPageSize(), records);
    }

    @Override
    public void delete(Long id) {}
}
