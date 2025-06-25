package com.taotao.cloud.ai.alibaba.tool_calling.component.time.function;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.taotao.cloud.ai.alibaba.tool_calling.component.time.TimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Function;

public class GetCurrentTimeByTimeZoneIdService implements Function<GetCurrentTimeByTimeZoneIdService.Request, GetCurrentTimeByTimeZoneIdService.Response> {

    private static final Logger logger = LoggerFactory.getLogger(GetCurrentTimeByTimeZoneIdService.class);

    @Override
    public Response apply(Request request) {
        String timeZoneId = request.timeZoneId;
        logger.info("The current time zone is {}", timeZoneId);
        return new Response(String.format("The current time zone is %s and the current time is " + "%s", timeZoneId,
                TimeUtils.getTimeByZoneId(timeZoneId)));
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonClassDescription("Get the current time based on time zone id")
    public record Request(@JsonProperty(required = true, value = "timeZoneId") @JsonPropertyDescription("Time zone id, such as Asia/Shanghai") String timeZoneId) {
    }

    public record Response(String description) {
    }

}
