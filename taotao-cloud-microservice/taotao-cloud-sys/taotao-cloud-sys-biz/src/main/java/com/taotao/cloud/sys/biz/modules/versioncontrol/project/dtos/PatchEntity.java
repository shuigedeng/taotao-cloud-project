package com.taotao.cloud.sys.biz.modules.versioncontrol.project.dtos;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

/**
 * 保存增量实体对象, 某在,在某个时间在哪个库的哪个分支打了增量
 */
@Getter
@Setter
public class PatchEntity {
    /**
     * 当前增量说明
     */
    private String title;
    /**
     * 分组
     */
    private String group;
    /**
     * 仓库
     */
    private String repository;
    /**
     * 分支
     */
    private String branch;
    /**
     * 增量时间
     */
    private long time;
    /**
     * 打增量的人
     */
    private String user;
    /**
     * 文件相对路径, 相对于临时路径的路径
     */
    private String filePath;

    /**
     * 是否还有效, 当增量文件被删除时,会被标记为失效
     */
    private boolean effect;

    public PatchEntity() {
    }

    public PatchEntity(String title, String group, String repository, String branch, long time, String user, String filePath, boolean effect) {
        this.title = title;
        this.group = group;
        this.repository = repository;
        this.branch = branch;
        this.time = time;
        this.user = user;
        this.filePath = filePath;
        this.effect = effect;
    }

    @Override
    public String toString() {
        final String[] fields = {title,group,repository,branch,time+"",user,filePath,effect+""};
        return StringUtils.join(fields,':');
    }
}
