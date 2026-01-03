package com.taotao.cloud.gradle.plugin;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

/**
 * SayHelloTask
 *
 * @author shuigedeng
 * @version 2026.02
 * @since 2025-12-19 09:30:45
 */
public class SayHelloTask extends DefaultTask {

    @TaskAction
    public void hello() {
        System.out.println("Hello, World!");
    }
}
