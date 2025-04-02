package com.taotao.cloud.generator.maku.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 项目名变更
 *
 * @author 阿沐 babamu@126.com
 * <a href="https://maku.net">MAKU</a>
 */

@Data
@TableName("gen_project_modify")
public class ProjectModifyEntity {
    /**
     * id
     */
    @TableId
    private Long id;

    /**
     * 项目名
     */
    private String projectName;

    /**
     * 项目标识
     */
    private String projectCode;

    /**
     * 项目包名
     */
    private String projectPackage;

    /**
     * 项目路径
     */
    private String projectPath;

    /**
     * 变更项目名
     */
    private String modifyProjectName;

    /**
     * 变更标识
     */
    private String modifyProjectCode;

    /**
     * 变更包名
     */
    private String modifyProjectPackage;

    /**
     * 排除文件
     */
    private String exclusions;

    /**
     * 变更文件
     */
    private String modifySuffix;

    /**
     * 创建时间
     */
    private Date createTime;

}
