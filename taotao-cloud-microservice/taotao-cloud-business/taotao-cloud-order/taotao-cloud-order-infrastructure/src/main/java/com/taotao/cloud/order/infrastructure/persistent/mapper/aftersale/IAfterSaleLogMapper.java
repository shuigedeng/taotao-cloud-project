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

package com.taotao.cloud.order.infrastructure.persistent.mapper.aftersale;

import com.taotao.cloud.order.infrastructure.persistent.po.aftersale.AfterSaleLogPO;
import com.taotao.boot.webagg.mapper.BaseSuperMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 售后日志数据处理层
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 08:49:21
 */
@Mapper
public interface IAfterSaleLogMapper extends BaseSuperMapper<AfterSaleLogPO, Long> {}
