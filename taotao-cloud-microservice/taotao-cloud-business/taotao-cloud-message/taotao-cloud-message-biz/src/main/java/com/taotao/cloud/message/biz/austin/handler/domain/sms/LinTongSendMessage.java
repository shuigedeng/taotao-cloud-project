package com.taotao.cloud.message.biz.austin.handler.domain.sms;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <span>Form File</span>
 * <p>Description</p>
 * <p>Company:QQ 752340543</p>
 *
 * @author topsuder
 * @version v1.0.0
 * @DATE 2022/11/24-15:58
 * @Description
 * @see com.taotao.cloud.message.biz.austin.handler.domain.sms austin
 */
@Data
@Accessors(chain=true)
public class LinTongSendMessage {
    String phone;
    String content;
}
