package com.taotao.cloud.gradle.springboot.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import com.taotao.cloud.gradle.springboot.plugin.tasks.DeleteExpand
import com.taotao.cloud.gradle.springboot.plugin.dependency.ManagementPlugin
import com.taotao.cloud.gradle.springboot.plugin.dependency.OptionalPlugin
import com.taotao.cloud.gradle.springboot.plugin.dependency.CompileProcessorPlugin
import com.taotao.cloud.gradle.springboot.plugin.info.ManifestPlugin
/**
 * <p>
 * CorePlugin
 * </p>
 *
 */
class CorePlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        project.pluginManager.apply(DeleteExpand.class)
        project.pluginManager.apply(ManagementPlugin.class)
        project.pluginManager.apply(OptionalPlugin.class)
        project.pluginManager.apply(CompileProcessorPlugin.class)
        project.pluginManager.apply(ManifestPlugin.class)
    }
}
