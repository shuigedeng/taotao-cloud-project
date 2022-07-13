package com.taotao.cloud.im.biz.platform.modules.chat.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class MyVo01 {

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    private String password;

    /**
     * 新密码
     */
    @NotBlank(message = "新密码不能为空")
    private String pwd;

}
