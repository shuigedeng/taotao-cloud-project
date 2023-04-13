package com.taotao.cloud.gradle.springboot.plugin.info

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.tasks.bundling.Jar
import org.gradle.util.GradleVersion
/**
 * <p>
 * JarPlugin
 * </p>
 *
 *
 */
abstract class ManifestPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.pluginManager.apply(JavaPlugin.class)
        project.tasks.withType(Jar.class).every {
            def attributes = it.manifest.attributes
            attributes.putIfAbsent("Implementation-Group", project.group)
            attributes.putIfAbsent("Implementation-Title", project.name)
            attributes.putIfAbsent("Implementation-Version", project.version)
            attributes.putIfAbsent("Created-By", System.getProperty("java.version") + " (" + System.getProperty("java.specification.vendor") + ")")
            attributes.putIfAbsent("Gradle-Version", GradleVersion.current())
        }
    }
}
