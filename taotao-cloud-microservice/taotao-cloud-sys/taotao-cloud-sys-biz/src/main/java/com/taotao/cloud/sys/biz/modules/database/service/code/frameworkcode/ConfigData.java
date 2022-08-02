package com.taotao.cloud.sys.biz.modules.database.service.code.frameworkcode;

import com.sanri.tools.modules.database.service.code.dtos.ProjectGenerateConfig;
import com.sanri.tools.modules.database.service.dtos.meta.TableMeta;
import lombok.Data;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class ConfigData {
    private ProjectGenerateConfig projectGenerateConfig;
    private File projectDir;
    private File javaDir;
    private File resourcesDir;
    private List<TableMeta> tableMetas = new ArrayList<>();

    public ConfigData(ProjectGenerateConfig projectGenerateConfig, File projectDir, File javaDir, File resourcesDir, List<TableMeta> tableMetas) {
        this.projectGenerateConfig = projectGenerateConfig;
        this.projectDir = projectDir;
        this.javaDir = javaDir;
        this.resourcesDir = resourcesDir;
        this.tableMetas = tableMetas;
    }

    /**
     * 中间生成的临时信息
     */
    private Map<String,Object> otherConfig = new HashMap<>();
}
