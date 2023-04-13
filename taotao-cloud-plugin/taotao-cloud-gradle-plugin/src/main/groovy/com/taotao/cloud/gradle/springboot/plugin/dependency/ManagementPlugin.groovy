package com.taotao.cloud.gradle.springboot.plugin.dependency

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.JavaTestFixturesPlugin
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin

/**
 * <p>
 * 创建dependency效果的BOM引入器
 * </p>
 *
 *
 */
abstract class ManagementPlugin implements Plugin<Project> {

    public static final String MANAGEMENT = "management"

    public static final Set<String> DEPENDENCY_NAMES_SET = new HashSet<>()

    static {
        DEPENDENCY_NAMES_SET.addAll(
                Set.of(
                        JavaPlugin.COMPILE_CLASSPATH_CONFIGURATION_NAME,
                        JavaPlugin.RUNTIME_CLASSPATH_CONFIGURATION_NAME,
                        JavaPlugin.ANNOTATION_PROCESSOR_CONFIGURATION_NAME,
                        JavaPlugin.TEST_COMPILE_CLASSPATH_CONFIGURATION_NAME,
                        JavaPlugin.TEST_RUNTIME_CLASSPATH_CONFIGURATION_NAME,
                        JavaPlugin.TEST_ANNOTATION_PROCESSOR_CONFIGURATION_NAME
                )
        )
    }

    @Override
    void apply(Project project) {
        def configurations = project.configurations
        project.pluginManager.apply(JavaPlugin.class)
        configurations.create(MANAGEMENT) { management ->
            management.visible = false
            management.canBeResolved = false
            management.canBeConsumed = false
            def plugins = project.plugins
            plugins.withType(JavaPlugin.class).every {
                DEPENDENCY_NAMES_SET.forEach { configurations.named(it).get().extendsFrom(management) }
            }
            plugins.withType(JavaTestFixturesPlugin.class).every {
                configurations.named("testFixturesCompileClasspath").get().extendsFrom(management)
                configurations.named("testFixturesRuntimeClasspath").get().extendsFrom(management)
            }
            plugins.withType(MavenPublishPlugin.class).every {
                project.extensions.getByType(PublishingExtension.class).publications
                        .withType(MavenPublication.class).every { mavenPublication ->
                    mavenPublication.versionMapping { versions ->
                        versions.allVariants {
                            it.fromResolutionResult()
                        }
                    }
                }
            }
        }
    }
}
