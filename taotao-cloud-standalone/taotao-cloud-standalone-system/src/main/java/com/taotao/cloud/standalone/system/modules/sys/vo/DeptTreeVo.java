package com.taotao.cloud.standalone.system.modules.sys.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @Classname DeptTreeVo
 * @Description 构建部门树vo
 * @Author Created by Lihaodong (alias:小东啊) lihaodongmail@163.com
 * @Date 2019-06-09 15:15
 * @Version 1.0
 */
@Setter
@Getter
@ToString
public class DeptTreeVo {

    private int id;
    private String label;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<DeptTreeVo> children;

}
