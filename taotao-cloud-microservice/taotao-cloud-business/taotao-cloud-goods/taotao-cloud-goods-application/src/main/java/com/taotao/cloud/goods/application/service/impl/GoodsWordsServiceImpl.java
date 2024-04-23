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

package com.taotao.cloud.goods.application.service.impl;

import com.taotao.cloud.goods.application.service.IGoodsWordsService;
import com.taotao.cloud.goods.infrastructure.persistent.mapper.IGoodsWordsMapper;
import com.taotao.cloud.goods.infrastructure.persistent.po.GoodsWords;
import com.taotao.cloud.goods.infrastructure.persistent.repository.cls.GoodsWordsRepository;
import com.taotao.cloud.goods.infrastructure.persistent.repository.inf.IGoodsWordsRepository;
import com.taotao.cloud.web.base.service.impl.BaseSuperServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 商品关键字业务层实现
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:02:50
 */
@Service
public class GoodsWordsServiceImpl
        extends BaseSuperServiceImpl<GoodsWords, Long, IGoodsWordsMapper, GoodsWordsRepository, IGoodsWordsRepository>
        implements IGoodsWordsService {}
