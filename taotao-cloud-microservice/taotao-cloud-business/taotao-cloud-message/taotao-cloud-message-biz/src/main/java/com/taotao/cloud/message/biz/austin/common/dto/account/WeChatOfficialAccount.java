package com.taotao.cloud.message.biz.austin.common.dto.account;

import lombok.*;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.*;
import lombok.NoArgsConstructor;

/**
 * 模板消息参数
 * <p>
 * 参数示例：
 * https://developers.weixin.qq.com/doc/offiaccount/Message_Management/Template_Message_Interface.html
 * * @author zyg
 */
@Data
@Accessors(chain=true)
@AllArgsConstructor
@NoArgsConstructor
public class WeChatOfficialAccount {

    /**
     * 账号相关
     */
    private String appId;
    private String secret;
    private String token;

}
