package com.taotao.cloud.gradle.springboot.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlatformExtension
import org.gradle.api.plugins.JavaPlatformPlugin
import com.taotao.cloud.gradle.springboot.plugin.maven.DeployedPlugin
/**
 * <p>
 * BomPlugin
 * </p>
 *
 *
 */
class BomPlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        project.pluginManager.apply(JavaPlatformPlugin.class)
        project.pluginManager.apply(DeployedPlugin.class)

        project.extensions.getByType(JavaPlatformExtension.class).allowDependencies()
    }
}
