package com.taotao.cloud.im.biz.platform.modules.chat.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class GroupVo01 {

    @NotNull(message = "群id不能为空")
    private Long groupId;

    @NotNull(message = "好友列表不能为空")
    private List<Long> list;
}
