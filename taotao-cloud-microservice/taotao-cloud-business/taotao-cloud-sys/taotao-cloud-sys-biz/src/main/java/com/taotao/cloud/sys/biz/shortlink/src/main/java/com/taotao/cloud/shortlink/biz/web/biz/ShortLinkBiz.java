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

package com.taotao.cloud.sys.biz.shortlink.src.main.java.com.taotao.cloud.shortlink.biz.web.biz;

import com.taotao.cloud.log.api.api.dto.ShortLinkDTO;
import com.taotao.cloud.log.api.api.enums.BooleanEnum;
import com.taotao.cloud.log.api.api.request.ShortLinkListRequest;
import com.taotao.cloud.shortlink.biz.web.invoker.ShortLinkInvoker;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * This is Description
 *
 * @since 2022/05/06
 */
@Slf4j
@Component
public class ShortLinkBiz {

    @Resource
    private ShortLinkInvoker shortLinkInvoker;

    public boolean checkShortLinkCodeValid(ShortLinkDTO shortLinkDTO) {
        if (Objects.nonNull(shortLinkDTO)) {
            return BooleanEnum.TRUE.getCode().equals(shortLinkDTO.getState())
                    && !shortLinkDTO.getExpired().isBefore(LocalDate.now());
        }

        return false;
    }

    public Optional<String> parseShortLinkCode(String shortLinkCode) {

        ShortLinkListRequest queryRequest = ShortLinkListRequest.builder()
                .shortLinkCodeSet(Collections.singleton(shortLinkCode))
                .build();
        List<ShortLinkDTO> shortLinkDtoList = shortLinkInvoker.listShortLinkCode(queryRequest);

        if (CollectionUtils.isNotEmpty(shortLinkDtoList)) {
            ShortLinkDTO shortLinkDTO = shortLinkDtoList.get(0);
            if (BooleanEnum.TRUE.getCode().equals(shortLinkDTO.getState())) {
                return Optional.ofNullable(shortLinkDTO.getOriginUrl());
            }
        }

        return Optional.empty();
    }

    public Optional<ShortLinkDTO> getShortLinkCodeDto(String shortLinkCode) {

        ShortLinkListRequest queryRequest = ShortLinkListRequest.builder()
                .shortLinkCodeSet(Collections.singleton(shortLinkCode))
                .build();
        List<ShortLinkDTO> shortLinkDtoList = shortLinkInvoker.listShortLinkCode(queryRequest);

        if (CollectionUtils.isNotEmpty(shortLinkDtoList)) {
            ShortLinkDTO shortLinkDTO = shortLinkDtoList.get(0);
            if (BooleanEnum.TRUE.getCode().equals(shortLinkDTO.getState())) {
                return Optional.of(shortLinkDTO);
            }
        }

        return Optional.empty();
    }

    public Boolean updateShortLinkCode(ShortLinkDTO shortLink) {

        // TODO

        return Boolean.TRUE;
    }
}
