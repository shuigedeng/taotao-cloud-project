package com.taotao.cloud.gradle.springboot.plugin
import com.taotao.cloud.gradle.springboot.plugin.info.BootPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin

/**
 * <p>
 * ServicePlugin
 * </p>
 *
 */
class ServicePlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        project.pluginManager.apply(JavaPlugin.class)
        project.pluginManager.apply(ModulePlugin.class)
        project.pluginManager.apply(BootPlugin.class)
    }
}
