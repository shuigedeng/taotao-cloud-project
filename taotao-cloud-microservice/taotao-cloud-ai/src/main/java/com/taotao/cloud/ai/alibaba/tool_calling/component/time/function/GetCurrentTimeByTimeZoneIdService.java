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

package com.taotao.cloud.ai.alibaba.tool_calling.component.time.function;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.taotao.cloud.ai.alibaba.tool_calling.component.time.TimeUtils;
import java.util.function.Function;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetCurrentTimeByTimeZoneIdService
        implements Function<
                GetCurrentTimeByTimeZoneIdService.Request,
                GetCurrentTimeByTimeZoneIdService.Response> {

    private static final Logger logger =
            LoggerFactory.getLogger(GetCurrentTimeByTimeZoneIdService.class);

    @Override
    public Response apply(Request request) {
        String timeZoneId = request.timeZoneId;
        logger.info("The current time zone is {}", timeZoneId);
        return new Response(
                String.format(
                        "The current time zone is %s and the current time is " + "%s",
                        timeZoneId, TimeUtils.getTimeByZoneId(timeZoneId)));
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonClassDescription("Get the current time based on time zone id")
    public record Request(
            @JsonProperty(required = true, value = "timeZoneId")
                    @JsonPropertyDescription("Time zone id, such as Asia/Shanghai")
                    String timeZoneId) {}

    public record Response(String description) {}
}
