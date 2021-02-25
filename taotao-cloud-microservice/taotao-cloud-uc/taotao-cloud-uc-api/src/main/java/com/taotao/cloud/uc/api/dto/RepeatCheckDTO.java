package com.taotao.cloud.uc.api.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 重复校验DTO
 *
 * @author dengtao
 * @date 2020/5/2 16:40
*/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "用户注册VO")
public class RepeatCheckDTO {

    /**
     * 字段值 邮箱 手机号 用户名
     */
    private String fieldVal;
    /**
     * 指用户id 主要作用编辑情况过滤自己的校验
     */
    private Integer dataId;

}
