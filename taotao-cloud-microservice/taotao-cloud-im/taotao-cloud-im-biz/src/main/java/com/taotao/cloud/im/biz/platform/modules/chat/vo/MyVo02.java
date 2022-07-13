package com.taotao.cloud.im.biz.platform.modules.chat.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class MyVo02 {

    @NotBlank(message = "头像不能为空")
    @Size(max = 2000, message = "头像长度不能大于2000")
    private String portrait;

}
