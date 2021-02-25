package com.taotao.cloud.uc.api.vo;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author dengtao
 * @date 2020/5/14 10:44
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "用户注册VO")
public class SysDeptTreeVo {

    /**
     * 对应SysDepart中的id字段,前端数据树中的key
     */
    private int key;
    /**
     * 对应SysDepart中的id字段,前端数据树中的value
     */
    private String value;
    /**
     * 对应depart_name字段,前端数据树中的title
     */
    private String title;

    /**
     * 部门主键ID
     */
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

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;

    /**
     * 是否删除  -1：已删除  0：正常
     */
    private String delFlag;

    /**
     * 上级部门
     */
    private String parentName;
    /**
     * 等级
     */
    private Integer level;

    private List<SysDeptTreeVo> children;
}
