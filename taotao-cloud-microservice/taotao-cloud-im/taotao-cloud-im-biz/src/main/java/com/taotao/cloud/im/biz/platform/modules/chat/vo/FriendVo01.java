package com.taotao.cloud.im.biz.platform.modules.chat.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class FriendVo01 {

    @NotBlank(message = "搜索参数不能为空")
    private String param;

}
