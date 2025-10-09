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

package com.taotao.cloud.message.biz.service.dubbo;

import com.taotao.cloud.sys.api.dubbo.DictItemRpcService;
import com.taotao.cloud.sys.biz.mapper.IDictItemMapper;
import com.taotao.cloud.sys.biz.model.entity.dict.DictItem;
import com.taotao.cloud.sys.biz.repository.DictItemRepository;
import com.taotao.cloud.sys.biz.repository.IDictItemRepository;
import com.taotao.boot.webagg.service.impl.BaseSuperServiceImpl;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;

/**
 * DictItemServiceImpl
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-10-09 20:34:52
 */
@Service
@DubboService(interfaceClass = DictItemRpcService.class, validation = "true")
public class DubboDictItemServiceImplService
        extends BaseSuperServiceImpl< DictItem, Long,IDictItemMapper, DictItemRepository, IDictItemRepository>
        implements DictItemRpcService {}
