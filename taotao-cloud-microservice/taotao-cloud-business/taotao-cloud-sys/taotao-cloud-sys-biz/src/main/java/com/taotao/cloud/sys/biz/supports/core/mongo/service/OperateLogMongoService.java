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
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.hutool.core.data.id.IdUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * MongoDB存储实现
 *
 * @author shuigedeng
 * @since 2021/12/2
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
        ExampleMatcher matching = ExampleMatcher.matching().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example<OperateLogMongo> example = Example.of(LogConvert.CONVERT.convert(operateLogParam), matching);

        // 设置分页条件 (第几页，每页大小，排序)
        Sort sort = Sort.by(Sort.Order.desc("id"));
        Pageable pageable = PageRequest.of(operateLogParam.getCurrentPage() - 1, operateLogParam.getPageSize(), sort);

        Page<OperateLogMongo> page = repository.findAll(example, pageable);
        List<OperateLogDto> records =
                page.getContent().stream().map(OperateLogMongo::toDto).toList();

        return PageResult.of(
                page.getTotalElements(), 1, operateLogParam.getCurrentPage(), operateLogParam.getPageSize(), records);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
