package com.taotao.cloud.sys.biz.modules.versioncontrol.project;

import com.sanri.tools.modules.core.service.file.FileManager;
import com.sanri.tools.modules.core.utils.OnlyPath;
import com.sanri.tools.modules.versioncontrol.dtos.ProjectLocation;
import com.sanri.tools.modules.versioncontrol.git.RepositoryMetaService;
import com.sanri.tools.modules.versioncontrol.project.dtos.ProjectMeta;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service
@Slf4j
public class ProjectMetaService {
    @Autowired
    private RepositoryMetaService repositoryMetaService;
    @Autowired
    private FileManager fileManager;

    /**
     * 添加或者获取一个项目, 在仓库中
     * @param projectName
     * @param path
     * @return
     */
    public ProjectMeta computeIfAbsent(ProjectLocation projectLocation) throws IOException {
        final RepositoryMetaService.RepositoryMeta repositoryMeta = repositoryMetaService.repositoryMeta(projectLocation.getGroup(), projectLocation.getRepository());

        final String projectName = projectLocation.getProjectName();

        final Map<String, ProjectMeta> projectMetaMap = repositoryMeta.getProjectMetaMap();
        if (projectMetaMap.containsKey(projectName)){
            return projectMetaMap.get(projectName);
        }
        final ProjectMeta projectMeta = new ProjectMeta(projectName,projectLocation.getPath());
        repositoryMeta.addProjectMeta(projectMeta);
        return projectMeta;
    }

    /**
     * 生成或者获取一个 ModuleCopileMeta
     * @param projectDir
     * @param relativePomFile
     */
    public ProjectMeta.ModuleCompileMeta resolveModuleCompileMeta(ProjectLocation projectLocation, String relativePomFile) throws IOException {
        final ProjectMeta projectMeta = computeIfAbsent(projectLocation);
        final String moduleName = new OnlyPath(relativePomFile).getParent().getFileName();
        ProjectMeta.ModuleCompileMeta moduleCompileMeta = projectMeta.getModuleCompileMetas().get(moduleName);
        if (moduleCompileMeta != null){
            return moduleCompileMeta;
        }
        moduleCompileMeta = new ProjectMeta.ModuleCompileMeta(moduleName,relativePomFile);
        projectMeta.getModuleCompileMetas().put(moduleName,moduleCompileMeta);
        return moduleCompileMeta;
    }

    /**
     * 写入模块元数据的 classpath
     * @param moduleCompileMeta
     * @param classpath classpath 路径列表
     */
    public void writeModuleClasspath(ProjectMeta.ModuleCompileMeta moduleCompileMeta,String classpath) throws IOException {
        final String pomFileRelativePath = moduleCompileMeta.getPomFileRelativePath();
        final String relativePath = new OnlyPath(pomFileRelativePath).getParent().toString();
        final File classPathDir = fileManager.mkTmpDir("classpath/" + relativePath);
        final File classPathFile = new File(classPathDir, System.currentTimeMillis() + "");
        final OnlyPath relativize = new OnlyPath(fileManager.getTmpBase()).relativize(new OnlyPath(classPathFile));
        FileUtils.writeStringToFile(classPathFile,classpath, StandardCharsets.UTF_8);
        moduleCompileMeta.setClasspath(relativize.toString());
    }

    /**
     * 读取模块元数据的 classpath
     * @param moduleCompileMeta
     */
    public String readModuleClasspath(ProjectMeta.ModuleCompileMeta moduleCompileMeta) throws IOException {
        final String classpath = moduleCompileMeta.getClasspath();
        if (StringUtils.isBlank(classpath)){
            return "";
        }
        final File file = new OnlyPath(classpath).resolveFile(fileManager.getTmpBase());
        if (file.exists()){
            return FileUtils.readFileToString(file,StandardCharsets.UTF_8);
        }
        return null;
    }

    /**
     * 获取模块 classpath 最后更新时间
     * @param moduleCompileMeta
     * @return
     */
    public Long readModuleClassPathLastUpdateTime(ProjectMeta.ModuleCompileMeta moduleCompileMeta){
        final String classpath = moduleCompileMeta.getClasspath();
        if (StringUtils.isBlank(classpath)){
            return null;
        }
        final File file = new OnlyPath(classpath).resolveFile(fileManager.getTmpBase());
        if (!file.exists()){
            return null;
        }
        return file.lastModified();
    }
}
