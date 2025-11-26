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

package com.taotao.cloud.shortlink.biz.web.invoker;

import com.alibaba.fastjson2.JSONObject;
import com.taotao.cloud.log.api.api.dto.ShortLinkDTO;
import com.taotao.cloud.log.api.api.request.ShortLinkListRequest;
import com.taotao.cloud.log.api.api.service.ShortLinkService;
import com.taotao.cloud.shortlink.biz.web.utils.CommonBizUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * This is Description
 *
 * @since 2022/05/06
 */
@Slf4j
@Component
public class ShortLinkInvoker {

    @DubboReference(version = "1.0.0", timeout = 5000)
    private ShortLinkService shortLinkService;

    public List<ShortLinkDTO> listShortLinkCode(ShortLinkListRequest request) {
        try {
            CommonResponse<List<ShortLinkDTO>> response = shortLinkService.listShortLinkCode(request);
            if (CommonBizUtil.isSuccessResponse(response)) {
                return Optional.ofNullable(response.getData()).orElse(Collections.emptyList());
            }

            log.warn("listShortLinkCode: 查短链失败, request -> {}", JSONObject.toJSONString(request));
        } catch (Exception e) {
            log.warn("listShortLinkCode: 查短链错误, request -> {},e -> {}", JSONObject.toJSONString(request), e.toString());
        }

        return Collections.emptyList();
    }
}
