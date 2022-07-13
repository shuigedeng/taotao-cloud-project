package com.taotao.cloud.im.biz.platform.modules.collect.vo;

import com.platform.modules.collect.enums.CollectTypeEnum;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class CollectVo01 {

    @NotNull(message = "收藏类型不能为空")
    private CollectTypeEnum collectType;

    @NotBlank(message = "收藏内容不能为空")
    @Size(max = 20000, message = "收藏内容长度不能大于20000")
    private String content;

}
