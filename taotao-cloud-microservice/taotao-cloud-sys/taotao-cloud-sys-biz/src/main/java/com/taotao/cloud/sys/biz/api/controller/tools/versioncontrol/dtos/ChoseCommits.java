package com.taotao.cloud.sys.biz.api.controller.tools.versioncontrol.dtos;

import lombok.Data;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Data
public class ChoseCommits {
    @Valid
    private ProjectLocation projectLocation;
    private List<String> commitIds = new ArrayList<>();
}
