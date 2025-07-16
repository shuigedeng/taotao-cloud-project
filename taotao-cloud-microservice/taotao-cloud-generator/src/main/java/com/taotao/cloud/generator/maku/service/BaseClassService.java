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

package com.taotao.cloud.generator.maku.service;

import com.taotao.cloud.generator.maku.common.page.PageResult;
import com.taotao.cloud.generator.maku.common.query.Query;
import com.taotao.cloud.generator.maku.common.service.BaseService;
import com.taotao.cloud.generator.maku.entity.BaseClassEntity;
import java.util.List;

/**
 * 基类管理
 *
 * @author 阿沐 babamu@126.com
 * <a href="https://maku.net">MAKU</a>
 */
public interface BaseClassService extends BaseService<BaseClassEntity> {

    PageResult<BaseClassEntity> page(Query query);

    List<BaseClassEntity> getList();
}
