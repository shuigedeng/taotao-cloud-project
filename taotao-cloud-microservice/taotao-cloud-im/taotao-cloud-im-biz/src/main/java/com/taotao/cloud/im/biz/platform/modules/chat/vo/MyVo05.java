package com.taotao.cloud.im.biz.platform.modules.chat.vo;

import com.platform.common.enums.GenderEnum;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class MyVo05 {

    @NotNull(message = "性别不能为空")
    private GenderEnum gender;

}
