package com.taotao.cloud.standalone.system.modules.sys.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 租户表
 * </p>
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysTenant implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 租户id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 租户名称
     */
    private String name;

    /**
     * 租户编号
     */
    private String code;

    /**
     * 授权开始时间
     */
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    /**
     * 授权结束时间
     */
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    /**
     * 0正常 9-冻结
     */
    private int status;

    /**
     * 删除标记
     */
    private String delFlag;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;


}
