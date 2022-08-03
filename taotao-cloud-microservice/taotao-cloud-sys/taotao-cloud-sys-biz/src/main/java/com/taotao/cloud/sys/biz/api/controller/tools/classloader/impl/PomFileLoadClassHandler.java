package com.taotao.cloud.sys.biz.api.controller.tools.classloader.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.taotao.cloud.sys.biz.api.controller.tools.classloader.FileLoadClassHandler;
import com.taotao.cloud.sys.biz.api.controller.tools.classloader.dtos.LoadClassResponse;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import lombok.extern.slf4j.Slf4j;

/**
 * pom 文件加载处理
 */
@Component("xml_FileLoadClassHandler")
@Slf4j
public class PomFileLoadClassHandler implements FileLoadClassHandler {
    @Autowired
    private JarFileLoadClassHandler jarFileLoadClassHandler;
    @Autowired
    private MavenJarResolve mavenJarResolve;
    @Autowired
    private MavenDependencyResolve mavenDependencyResolve;

    @Override
    public void handle(Collection<File> pomFiles, File targetClassloaderDir, LoadClassResponse loadClassResponse) {
        // pom 其实就是加载出 jar 包, 使用 JarFileLoadClassHandler 来处理
        List<File> allJarFiles = new ArrayList<>();
        long startTime = System.currentTimeMillis();
        for (File pomFile : pomFiles) {
            log.info("当前加载 pom 文件[{}]", pomFile.getName());
            try {
                // 因为 root artifact 是示例, 不可能存在, 主要需要获取 dependencies 中的内容
                final Model model = mavenDependencyResolve.resolvePomModel(pomFile);
                final List<Dependency> dependencies = model.getDependencies();
                for (Dependency dependency : dependencies) {
                    Artifact artifact = new DefaultArtifact(dependency.getGroupId(),dependency.getArtifactId(),dependency.getType(),dependency.getVersion());
                    final JarCollect jarCollect = mavenJarResolve.resolveArtifact(loadClassResponse.getSettings(), artifact);
                    allJarFiles.addAll(jarCollect.getFiles());
                }

//                final JarCollect jarCollect = mavenJarResolve.resolveJarFiles(loadClassResponse.getSettings(), pomFile);
//                allJarFiles.addAll(jarCollect.getFiles());
            } catch (Exception e) {
                log.info("当前加载 pom 文件失败[{}],原因:{}", pomFile.getName(), e.getMessage());
            } finally {
                log.info("加载文件[{}]的依赖项当前耗时:{} ms", pomFile.getName(), (System.currentTimeMillis() - startTime));
            }
        }
        jarFileLoadClassHandler.handle(allJarFiles,targetClassloaderDir,loadClassResponse);
    }
}
