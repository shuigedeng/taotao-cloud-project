package com.taotao.cloud.uc.api.vo;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 构建部门树vo
 *
 * @author dengtao
 * @date 2020/5/2 16:42
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "用户注册VO")
public class DeptTreeVo {

    /**
     * 对应SysDepart中的id字段,前端数据树中的key
     */
    private Long key;
    /**
     * 对应SysDepart中的id字段,前端数据树中的value
     */
    private String value;
    /**
     * 对应depart_name字段,前端数据树中的title
     */
    private String title;

    private List<DeptTreeVo> children;

}
