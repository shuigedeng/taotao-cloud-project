package com.taotao.cloud.im.biz.platform.modules.chat.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class MyVo04 {

    @NotBlank(message = "图片不能为空")
    @Size(max = 2000, message = "昵称长度不能大于2000")
    private String images;

    @NotBlank(message = "内容不能为空")
    @Size(max = 2000, message = "内容长度不能大于2000")
    private String content;

}
