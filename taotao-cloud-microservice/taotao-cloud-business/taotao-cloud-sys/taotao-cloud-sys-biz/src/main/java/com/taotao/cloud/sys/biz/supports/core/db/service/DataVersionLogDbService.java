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

package com.taotao.cloud.sys.biz.supports.core.db.service;

import com.taotao.boot.common.model.result.PageResult;
import com.taotao.boot.common.utils.common.JsonUtils;
import com.taotao.boot.security.spring.utils.SecurityUtils;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 数据版本日志数据库实现
 *
 * @author shuigedeng
 * @since 2022/1/10
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
            dataVersionLog.setChangeContent((String) param.getChangeContent());
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
        return MpUtils.convertMybatisPage(manager.page(param), DataVersionLogDto.class);
    }

    /**
     * 删除
     */
    @Override
    public void delete(Long id) {}
}
