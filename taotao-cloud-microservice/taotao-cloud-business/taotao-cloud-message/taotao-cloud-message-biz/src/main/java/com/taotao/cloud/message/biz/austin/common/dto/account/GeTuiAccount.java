package com.taotao.cloud.message.biz.austin.common.dto.account;


import lombok.*;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.*;
import lombok.NoArgsConstructor;


/**
 * 创建个推账号时的元信息
 *
 * @author shuigedeng
 * <p>
 * （在调用个推的api时需要用到部分的参数）
 * <p>
 * https://docs.getui.com/getui/start/devcenter/
 */
@Data
@Accessors(chain=true)
@AllArgsConstructor
@NoArgsConstructor
public class GeTuiAccount {

    private String appId;

    private String appKey;

    private String masterSecret;
}
