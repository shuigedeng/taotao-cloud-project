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

package com.taotao.cloud.payment.biz.bootx.dto.notify;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author xxm
 * @date 2021/6/22
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "支付回调记录")
public class PayNotifyRecordDto extends BaseDto implements Serializable {
    private static final long serialVersionUID = -1241346974397068912L;

    @Schema(description = "支付号")
    private Long paymentId;

    @Schema(description = "通知消息")
    private String notifyInfo;

    @Schema(description = "支付通道")
    private Integer payChannel;

    @Schema(description = "处理状态")
    private Integer status;

    @Schema(description = "提示信息")
    private String msg;

    @Schema(description = "回调时间")
    private LocalDateTime notifyTime;
}
