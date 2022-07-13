package com.taotao.cloud.im.biz.platform.modules.topic.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class TopicVo02 {

    @NotBlank(message = "封面不能为空")
    @Size(max = 2000, message = "封面长度不能大于2000")
    private String cover;

}
