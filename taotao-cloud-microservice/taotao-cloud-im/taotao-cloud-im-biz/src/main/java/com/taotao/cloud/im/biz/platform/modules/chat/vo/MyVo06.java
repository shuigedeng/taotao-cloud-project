package com.taotao.cloud.im.biz.platform.modules.chat.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class MyVo06 {

    @NotBlank(message = "微聊号不能为空")
    @Size(min = 6, max = 20, message = "微聊号长度限6-20位")
    private String chatNo;

}
