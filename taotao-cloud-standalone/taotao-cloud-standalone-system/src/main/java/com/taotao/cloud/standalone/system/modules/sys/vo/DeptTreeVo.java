package com.taotao.cloud.standalone.system.modules.sys.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import jdk.nashorn.internal.objects.annotations.Getter;
import jdk.nashorn.internal.objects.annotations.Setter;

/**
 * @Classname DeptTreeVo
 * @Description 构建部门树vo
 * @Author shuigedeng
 * @since 2019-06-09 15:15
 * @Version 1.0
 */
public class DeptTreeVo {

    private int id;
    private String label;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<DeptTreeVo> children;

}
