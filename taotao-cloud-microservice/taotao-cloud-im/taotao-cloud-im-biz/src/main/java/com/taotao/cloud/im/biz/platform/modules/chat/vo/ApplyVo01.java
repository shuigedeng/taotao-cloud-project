package com.taotao.cloud.im.biz.platform.modules.chat.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ApplyVo01 {

    @NotNull(message = "申请id不能为空")
    private Long applyId;

}
