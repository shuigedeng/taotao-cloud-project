package com.taotao.cloud.uc.api.dto;

import io.swagger.annotations.ApiModel;
import lombok.*;

/**
 * dengtao
 *
 * @author dengtao
 * @date 2020/6/15 11:00
*/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "用户注册VO")
public class DeptDTO {

    private static final long serialVersionUID = 1L;

    private Integer deptId;

    /**
     * 部门名称
     */
    private String name;

    /**
     * 上级部门
     */
    private Integer parentId;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 备注
     */
    private String remark;


}
