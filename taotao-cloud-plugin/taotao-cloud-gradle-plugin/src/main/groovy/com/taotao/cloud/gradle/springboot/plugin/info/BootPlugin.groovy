package com.taotao.cloud.gradle.springboot.plugin.info

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.tasks.bundling.Jar
import org.springframework.boot.gradle.dsl.SpringBootExtension
import org.springframework.boot.gradle.plugin.SpringBootPlugin
import org.springframework.boot.gradle.tasks.bundling.BootJar

import java.time.Instant
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit

/**
 * <p>
 * BuildInfoPlugin
 * </p>
 *
 *
 */
abstract class BootPlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        project.pluginManager.apply(JavaPlugin.class)
        project.pluginManager.apply(SpringBootPlugin.class)
        project.extensions.getByType(SpringBootExtension.class)
                .buildInfo {
                    it.properties { build ->
                        build.group.set(project.group.toString())
                        build.version.set(project.version.toString())
                        build.time.set(DateTimeFormatter.ISO_INSTANT.format(Instant.now().plusMillis(TimeUnit.HOURS.toMillis(8))))
                    }
                }
        def bootJar = project.tasks.named(SpringBootPlugin.BOOT_JAR_TASK_NAME).get() as BootJar
        bootJar.archiveBaseName.set(project.name)
        bootJar.archiveFileName.set(bootJar.archiveBaseName.get() + "." + bootJar.archiveExtension.get())
        bootJar.launchScript()
        (project.tasks.named(JavaPlugin.JAR_TASK_NAME).get() as Jar).enabled = false
    }
}
