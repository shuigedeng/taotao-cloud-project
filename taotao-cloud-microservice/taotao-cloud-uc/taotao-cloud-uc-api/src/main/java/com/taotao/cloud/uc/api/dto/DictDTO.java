package com.taotao.cloud.uc.api.dto;

import io.swagger.annotations.ApiModel;
import lombok.*;

/**
 * 字典dto
 *
 * @author dengtao
 * @date 2020/4/30 11:20
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "用户注册VO")
public class DictDTO {

    private Integer id;

    private String dictName;

    private String dictCode;

    private String description;

    private Integer sort;

    private String remark;

    private String value;
}
