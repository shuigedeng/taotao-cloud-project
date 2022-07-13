package com.taotao.cloud.im.biz.platform.modules.chat.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class MyVo07 {

    @NotBlank(message = "个性签名不能为空")
    @Size(min = 1, max = 200, message = "个性签名长度限1-200位")
    private String intro;

}
