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

package com.taotao.cloud.shorlink.api.api.service;

import com.taotao.boot.common.model.result.Result;
import com.taotao.cloud.shorlink.api.api.common.PageResult;
import com.taotao.cloud.shorlink.api.api.dto.ShortLinkDTO;
import com.taotao.cloud.shorlink.api.api.request.ShortLinkCreateRequest;
import com.taotao.cloud.shorlink.api.api.request.ShortLinkListRequest;
import com.taotao.cloud.shorlink.api.api.request.ShortLinkTimePageRequest;
import com.taotao.cloud.shorlink.api.api.request.ShortLinkUrlQueryRequest;

import java.util.List;

/**
 * 短链服务 - dubbo api
 *
 * <p>version: 1.0.0
 *
 * @since 2022/05/03
 */
public interface ShortLinkService {

    /**
     * 创建短链
     *
     * @param request @{@link ShortLinkCreateRequest}
     * @return @{@link ShortLinkDTO} 短链DTO
     */
    Result<Boolean> createShortLinkCode(ShortLinkCreateRequest request);

    /**
     * 批量查询短链 - by code
     *
     * @param request @{@link ShortLinkListRequest}
     * @return @{@link ShortLinkDTO} 短链DTO
     */
    Result<List<ShortLinkDTO>> listShortLinkCode(ShortLinkListRequest request);

    /**
     * 查询短链 - by originUrl
     *
     * @param request @{@link ShortLinkUrlQueryRequest}
     * @return @{@link ShortLinkDTO} 短链DTO
     */
    Result<List<ShortLinkDTO>> getShortLinkCodeByOriginUrl(ShortLinkUrlQueryRequest request);

    /**
     * 分页查询短链 - by time
     *
     * @param request @{@link ShortLinkTimePageRequest}
     * @return @{@link ShortLinkDTO} 短链DTO
     */
    Result<PageResult<ShortLinkDTO>> pageShortLinkByTime(ShortLinkTimePageRequest request);

    // TODO 根据创建人分页查询
    // TODO 根据备注模糊查询
}
