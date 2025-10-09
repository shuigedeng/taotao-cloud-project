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

package com.taotao.cloud.goods.biz.service.business.impl;

import com.taotao.cloud.goods.biz.mapper.IStudioCommodityMapper;
import com.taotao.cloud.goods.biz.model.entity.StudioCommodity;
import com.taotao.cloud.goods.biz.repository.StudioCommodityRepository;
import com.taotao.cloud.goods.biz.repository.IStudioCommodityRepository;
import com.taotao.cloud.goods.biz.service.business.IStudioCommodityService;
import com.taotao.boot.webagg.service.impl.BaseSuperServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 直播间-商品关联业务层实现
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:03:01
 */
@Service
public class StudioCommodityServiceImpl
        extends BaseSuperServiceImpl<
                StudioCommodity, Long, IStudioCommodityMapper, StudioCommodityRepository, IStudioCommodityRepository>
        implements IStudioCommodityService {}
