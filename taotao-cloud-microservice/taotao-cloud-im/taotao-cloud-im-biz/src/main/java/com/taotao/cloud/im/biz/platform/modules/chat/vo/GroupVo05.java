package com.taotao.cloud.im.biz.platform.modules.chat.vo;

import com.platform.common.enums.YesOrNoEnum;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class GroupVo05 {

    @NotNull(message = "群id不能为空")
    private Long groupId;

    @NotNull(message = "状态不能为空")
    private YesOrNoEnum disturb;
}
