package com.taotao.cloud.sys.biz.api.controller.tools.versioncontrol.git.dtos;

import com.taotao.cloud.sys.biz.api.controller.tools.versioncontrol.dtos.ProjectLocation;
import lombok.Data;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Data
public class TarFileParam {
    private ProjectLocation projectLocation;

    private List<String> commitIds = new ArrayList<>();
    private List<String> relativePaths = new ArrayList<>();

    /**
     * 增量简单描述
     */
    private String title;
    /**
     * 目录结构  struct: 层级结构 , parallel : 平级结构
     */
    private String struct;

    public boolean isParallel(){
        return StringUtils.isNoneBlank(struct) && "parallel".equals(struct);
    }

    public boolean isStruct(){
        return StringUtils.isBlank(struct) || "struct".equals(struct);
    }

    public String getGroup(){
        return projectLocation.getGroup();
    }
    public String getRepository(){
        return projectLocation.getRepository();
    }
}
