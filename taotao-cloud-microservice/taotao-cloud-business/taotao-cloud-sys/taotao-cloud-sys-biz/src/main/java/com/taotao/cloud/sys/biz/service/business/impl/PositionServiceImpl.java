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

package com.taotao.cloud.sys.biz.service.business.impl;

import com.taotao.cloud.sys.biz.mapper.IPositionMapper;
import com.taotao.cloud.sys.biz.model.entity.system.Position;
import com.taotao.cloud.sys.biz.repository.JobRepository;
import com.taotao.cloud.sys.biz.repository.IJobRepository;
import com.taotao.cloud.sys.biz.service.business.IPositionService;
import com.taotao.boot.webagg.service.impl.BaseSuperServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 岗位表服务实现类
 *
 * @author shuigedeng
 * @since 2020-10-16 16:23:05
 * @since 1.0
 */
@Service
public class PositionServiceImpl
        extends BaseSuperServiceImpl<Position, Long, IPositionMapper, JobRepository, IJobRepository>
        implements IPositionService {}
