package com.taotao.cloud.im.biz.platform.modules.sms.vo;

import com.platform.modules.sms.enums.SmsTypeEnum;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 用户登录对象
 */
@Data
public class SmsVo {
    /**
     * 手机号
     */
    @NotBlank(message = "手机号不能为空")
    private String phone;

    /**
     * 短信类型
     */
    @NotNull(message = "短信类型不能为空")
    private SmsTypeEnum type;

}
