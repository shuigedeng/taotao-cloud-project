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

package com.taotao.cloud.stock.biz.interfaces.facade;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 日志保存服务实现
 *
 * @author shuigedeng
 * @since 2021-06-21
 */
@DubboService(timeout = 3000)
public class LogSaveServiceImpl implements LogSaveService {

    @Autowired
    private LogRepository logRepository;

    @Override
    public void save(LogDTO logDTO) {
        logRepository.store(LogDTOAssembler.toLog(logDTO));
    }
}
