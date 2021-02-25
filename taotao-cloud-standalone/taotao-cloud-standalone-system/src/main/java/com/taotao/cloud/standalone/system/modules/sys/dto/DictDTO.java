package com.taotao.cloud.standalone.system.modules.sys.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Classname DictDTO
 * @Description 字典dto
 * @Author 李号东 lihaodongmail@163.com
 * @Date 2019-06-02 09:36
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class DictDTO {


    private Integer id;

    private String dictName;

    private String dictCode;

    private String description;

    private Integer sort;

    private String remark;
}
