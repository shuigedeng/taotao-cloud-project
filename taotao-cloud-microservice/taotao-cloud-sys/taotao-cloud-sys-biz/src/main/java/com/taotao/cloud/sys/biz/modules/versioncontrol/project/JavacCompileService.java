package com.taotao.cloud.sys.biz.modules.versioncontrol.project;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import com.taotao.cloud.sys.biz.modules.compiler.JavaCompilerService;
import com.taotao.cloud.sys.biz.modules.compiler.dtos.CompileResult;
import com.taotao.cloud.sys.biz.modules.compiler.dtos.ModuleCompileConfig;
import com.taotao.cloud.sys.biz.modules.core.utils.OnlyPath;
import com.taotao.cloud.sys.biz.modules.versioncontrol.dtos.ProjectLocation;
import com.taotao.cloud.sys.biz.modules.versioncontrol.project.dtos.PomFile;
import com.taotao.cloud.sys.biz.modules.versioncontrol.project.dtos.ProjectMeta;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;


import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class JavacCompileService {
    @Autowired
    private MavenProjectService mavenProjectService;
    @Autowired
    private JavaCompilerService javaCompilerService;
    @Autowired
    private ProjectMetaService projectMetaService;

    /**
     * 少量文件使用 javac 进行编译, 因为是按模块拿 classpath 的, 所以是按照模块来批量编译
     * @param projectLocation 项目路径
     * @param projectDir 项目目录
     * @param modifyFiles 修改的文件列表
     * @return
     */
    public Map<String, CompileResult> compileLittleFiles(ProjectLocation projectLocation, File projectDir, List<File> modifyFiles) throws IOException {
        Map<String,CompileResult> compileResultMap = new HashMap<>();
        // 找到变更的模块的 pom 文件信息, 相同模块进行合并 模块 pom 文件 => 变更文件列表
        MultiValueMap<File,File> pomFileFileMultiValueMap = new LinkedMultiValueMap<>();
        for (File modifyFile : modifyFiles) {
            final PomFile pomFile = mavenProjectService.findPomFile(modifyFile, projectDir);
            pomFileFileMultiValueMap.add(pomFile.getPomFile(),modifyFile);
        }

        // 找到模块依赖 jar 包列表
        final Iterator<File> iterator = pomFileFileMultiValueMap.keySet().iterator();
        while (iterator.hasNext()){
            final File pomFile = iterator.next();
            final List<File> files = pomFileFileMultiValueMap.get(pomFile);

            // 获取当前模块的 classpath 数据
            final OnlyPath relativize = new OnlyPath(projectDir).relativize(new OnlyPath(pomFile));
            final ProjectMeta.ModuleCompileMeta moduleCompileMeta = projectMetaService.resolveModuleCompileMeta(projectLocation, relativize.toString());
            final String moduleClasspath = projectMetaService.readModuleClasspath(moduleCompileMeta);
            final List<File> classpathFiles = ClasspathUtils.toFiles(moduleClasspath);

            final ModuleCompileConfig moduleCompileConfig = new ModuleCompileConfig();
            moduleCompileConfig.setModule(pomFile.getParentFile());
            moduleCompileConfig.setClasspaths(classpathFiles);
            moduleCompileConfig.setSourceRoot("src/main/java");
            final OnlyPath moduleRoot = new OnlyPath(pomFile.getParentFile());
            final List<String> compileFiles = files.stream().map(file -> moduleRoot.relativize(new OnlyPath(file))).map(OnlyPath::toString).collect(Collectors.toList());
            moduleCompileConfig.setCompileFiles(compileFiles);
            moduleCompileConfig.setOutput("target/classes");
            final CompileResult compileResult = javaCompilerService.compileModuleFiles(moduleCompileConfig);

            final String modulePath = new OnlyPath(projectDir).relativize(new OnlyPath(pomFile).getParent()).toString();
            compileResultMap.put(modulePath,compileResult);
        }

        return compileResultMap;
    }

    public static final class ClasspathUtils{
        /**
         * classpath 转成文件列表
         * @param classpath
         * @return
         */
        public static final List<File> toFiles(String classpath){
            if (StringUtils.isBlank(classpath)){
                return new ArrayList<>();
            }
            return Arrays.stream(classpath.split(";")).map(File::new).collect(Collectors.toList());
        }
    }
}
