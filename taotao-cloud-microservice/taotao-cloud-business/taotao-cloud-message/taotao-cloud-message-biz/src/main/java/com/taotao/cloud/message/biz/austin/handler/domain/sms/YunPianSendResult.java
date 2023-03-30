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

package com.taotao.cloud.message.biz.austin.handler.domain.sms;

import com.alibaba.fastjson.annotation.JSONField;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 云片网短信调用发送接口返回值
 *
 * @author 3y
 */
@NoArgsConstructor
@Data
public class YunPianSendResult {

    /** totalCount */
    @JSONField(name = "total_count")
    private Integer totalCount;
    /** totalFee */
    @JSONField(name = "total_fee")
    private String totalFee;
    /** unit */
    @JSONField(name = "unit")
    private String unit;
    /** data */
    @JSONField(name = "data")
    private List<DataDTO> data;

    /** DataDTO */
    @NoArgsConstructor
    @Data
    public static class DataDTO {

        /** httpStatusCode */
        @JSONField(name = "http_status_code")
        private Integer httpStatusCode;
        /** code */
        @JSONField(name = "code")
        private Integer code;
        /** msg */
        @JSONField(name = "msg")
        private String msg;
        /** count */
        @JSONField(name = "count")
        private Integer count;
        /** fee */
        @JSONField(name = "fee")
        private Integer fee;
        /** unit */
        @JSONField(name = "unit")
        private String unit;
        /** mobile */
        @JSONField(name = "mobile")
        private String mobile;
        /** sid */
        @JSONField(name = "sid")
        private String sid;
    }
}
