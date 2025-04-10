package com.taotao.cloud.message.biz.austin.common.dto.account;


import lombok.*;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.*;
import lombok.NoArgsConstructor;

/**
 * 企业微信 机器人 账号信息
 *
 * @author shuigedeng
 */
@Data
@Accessors(chain=true)
@AllArgsConstructor
@NoArgsConstructor
public class EnterpriseWeChatRobotAccount {
    /**
     * 自定义群机器人中的 webhook
     */
    private String webhook;

}
