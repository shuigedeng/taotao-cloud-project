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

package com.taotao.cloud.job.server.persistence.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.job.server.persistence.domain.InstanceInfo;
import com.taotao.cloud.job.server.persistence.mapper.InstanceInfoMapper;
import com.taotao.cloud.job.server.persistence.service.InstanceInfoService;
import org.springframework.stereotype.Service;

/**
 * @author shuigedeng
 * @description 针对表【instance_info】的数据库操作Service实现
 * @createDate 2024-10-20 20:12:43
 */
@Service
public class InstanceInfoServiceImpl extends ServiceImpl<InstanceInfoMapper, InstanceInfo>
        implements InstanceInfoService {}
