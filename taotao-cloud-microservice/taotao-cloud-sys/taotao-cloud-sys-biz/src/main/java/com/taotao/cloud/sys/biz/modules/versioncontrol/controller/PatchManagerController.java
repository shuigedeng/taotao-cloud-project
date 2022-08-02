package com.taotao.cloud.sys.biz.modules.versioncontrol.controller;

import com.taotao.cloud.sys.biz.modules.versioncontrol.project.PatchManager;
import com.taotao.cloud.sys.biz.modules.versioncontrol.project.dtos.PatchEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/patch")
public class PatchManagerController {
    @Autowired
    private PatchManager patchManager;

    /**
     * 列出仓库的历史补丁记录
     * @param group 分组名
     * @param repository 仓库名
     * @return
     */
    @GetMapping("/list")
    public List<PatchEntity> list(String group, String repository){
        return patchManager.listPatch(group,repository);
    }

    /**
     * 清空无效补丁包
     * @throws IOException
     */
    @PostMapping("/cleanNotEffectPatch")
    public void cleanNotEffectPatch() throws IOException {
        patchManager.cleanNotEffectPatch();
    }

    /**
     * 删除某个补丁包
     * @param filepath
     * @throws IOException
     */
    @PostMapping("/deletePatch")
    public void deletePatch(String filepath) throws IOException {
        patchManager.deletePatch(filepath);
    }
}
