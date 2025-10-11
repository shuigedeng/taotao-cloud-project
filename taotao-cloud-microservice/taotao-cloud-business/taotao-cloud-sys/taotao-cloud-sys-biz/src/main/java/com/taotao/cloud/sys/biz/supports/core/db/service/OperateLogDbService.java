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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/**
 * 操作日志
 *
 * @author shuigedeng
 * @since 2021/8/12
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
    public PageResult<OperateLogDto> page(OperateLogParam operateLogParam) {
        return MpUtils.convertMybatisPage(operateLogManager.page(operateLogParam), OperateLogDto.class);
    }

    /**
     * 删除
     */
    @Override
    public void delete(Long id) {
        operateLogManager.deleteById(id);
    }
}
