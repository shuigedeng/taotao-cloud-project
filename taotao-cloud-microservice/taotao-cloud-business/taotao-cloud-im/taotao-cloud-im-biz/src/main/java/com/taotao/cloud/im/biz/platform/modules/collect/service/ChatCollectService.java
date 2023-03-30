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

package com.taotao.cloud.im.biz.platform.modules.collect.service;

import com.github.pagehelper.PageInfo;
import com.platform.common.web.service.BaseService;
import com.platform.modules.collect.domain.ChatCollect;
import com.platform.modules.collect.vo.CollectVo01;

/** 收藏表 服务层 q3z3 */
public interface ChatCollectService extends BaseService<ChatCollect> {

    /** 新增收藏 */
    void addCollect(CollectVo01 collectVo);

    /** 删除收藏 */
    void deleteCollect(Long collectId);

    /** 列表 */
    PageInfo collectList(ChatCollect collect);
}
