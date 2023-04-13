package com.taotao.cloud.gradle.springboot.plugin
import com.taotao.cloud.gradle.springboot.plugin.compile.CompileArgsPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * <p>
 * ModulePlugin
 * </p>
 *
 * @author livk
 *
 */
class ModulePlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        project.pluginManager.apply(CompileArgsPlugin.class)
        project.pluginManager.apply(CorePlugin.class)
    }
}
