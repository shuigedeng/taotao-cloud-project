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

package com.taotao.cloud.message.biz.service.business;

import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.message.biz.model.entity.ShortLink;
import java.util.List;

/** 短链接 业务层 */
public interface ShortLinkService extends IService<ShortLink> {

    /**
     * 根据模型，查询返回的集合
     *
     * @param shortLink 短链接模型
     * @return 端链接集合
     */
    List<ShortLink> queryShortLinks(ShortLink shortLink);
}
