package com.taotao.cloud.sys.biz.api.controller.tools.versioncontrol.project.dtos;

import com.taotao.cloud.sys.biz.api.controller.tools.versioncontrol.dtos.ProjectLocation;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Data
public class MavenGoalsParam {
    @NotBlank
    private String settingsName;
    @Valid
    private ProjectLocation projectLocation;
    @NotBlank
    private String relativePomFile;
    private List<String> goals = new ArrayList<>();
}
