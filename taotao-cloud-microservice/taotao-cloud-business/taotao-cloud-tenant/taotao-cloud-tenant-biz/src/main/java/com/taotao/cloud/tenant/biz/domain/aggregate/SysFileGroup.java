package com.taotao.cloud.tenant.biz.domain.aggregate;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.mdframe.forge.starter.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 文件分组实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_file_group")
public class SysFileGroup extends BaseEntity {
    
    @TableId(type = IdType.ASSIGN_ID)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    /**
     * 分组名称
     */
    private String groupName;

    /**
     * 分组编码
     */
    private String groupCode;

    /**
     * 分组类型(document-文档,image-图片,video-视频,audio-音频,archive-压缩包,default-默认)
     */
    private String groupType;

    /**
     * 父分组ID
     */
    private Long parentId;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 图标
     */
    private String icon;

    /**
     * 描述
     */
    private String description;

    /**
     * 状态(1-正常,0-禁用)
     */
    private Integer status;

    /**
     * 删除标记(0-未删除,1-已删除)
     */
    @TableLogic
    private Integer deleted;
    
    @TableField(exist = false)
    private Integer fileCount;
}
