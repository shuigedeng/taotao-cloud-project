package com.taotao.cloud.sys.biz.modules.versioncontrol.project.dtos;

import com.taotao.cloud.sys.biz.modules.versioncontrol.dtos.ProjectLocation;
import lombok.Data;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Data
public class JavacCompileFiles {
    @Valid
    private ProjectLocation projectLocation;
    /**
     * 相对于项目根路径
     */
    private List<String> relativePaths = new ArrayList<>();

    public JavacCompileFiles() {
    }

    public JavacCompileFiles( @Valid ProjectLocation projectLocation, List<String> relativePaths) {
        this.projectLocation = projectLocation;
        this.relativePaths = relativePaths;
    }
}
