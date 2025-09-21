package com.taotao.cloud.gradle.springboot.plugin.compile

import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.api.tasks.javadoc.Javadoc
import org.gradle.api.tasks.testing.Test

/**
 * <p>
 * 添加编译参数
 * </p>
 *
 */
abstract class CompileArgsPlugin implements Plugin<Project> {

    private static final List<String> COMPILER_ARGS = new ArrayList<>()
    private static final String MAPSTRUCT_PROCESSOR_NAME = "mapstruct-processor"
    private static final List<String> MAPSTRUCT_COMPILER_ARGS = new ArrayList<>()
    private static final String UTF_8 = "UTF-8"

    static {
        COMPILER_ARGS.addAll(Arrays.asList("-Xlint:-options",
                "-Xlint:rawtypes",
                "-Xlint:deprecation",
                "-Xlint:unchecked",
                "-parameters"))
        MAPSTRUCT_COMPILER_ARGS.addAll(Arrays.asList("-Amapstruct.unmappedTargetPolicy=IGNORE"))
    }

    @Override
    void apply(Project project) {
        project.tasks.withType(Javadoc.class).every { javadoc ->
            javadoc.options {
                encoding(UTF_8)
            }
        }

        project.pluginManager.apply(JavaPlugin.class)
        def javaCompile = project.tasks.named(JavaPlugin.COMPILE_JAVA_TASK_NAME).get() as JavaCompile
        addCompile(javaCompile)

        def test = project.tasks.named(JavaPlugin.COMPILE_TEST_JAVA_TASK_NAME).getOrNull()
        if (test != null) {
            def javaTestCompile = test as JavaCompile
            addCompile(javaTestCompile)
        }

        project.tasks.withType(Test.class).every {
            it.useJUnitPlatform()
        }

        //在 Project 配置结束后调用
        project.afterEvaluate {
            def dependencyName = new HashSet<>()
            project.configurations.forEach {
                dependencyName.addAll(it.dependencies.name)
            }
            if (dependencyName.contains(MAPSTRUCT_PROCESSOR_NAME)) {
                javaCompile.options.compilerArgs.addAll(MAPSTRUCT_COMPILER_ARGS)
            }
        }
    }

    private static void addCompile(JavaCompile javaCompile) {
        javaCompile.options.compilerArgs.addAll(COMPILER_ARGS)
        javaCompile.options.encoding = UTF_8
        javaCompile.sourceCompatibility = JavaVersion.VERSION_25
        javaCompile.targetCompatibility = JavaVersion.VERSION_25
    }
}
