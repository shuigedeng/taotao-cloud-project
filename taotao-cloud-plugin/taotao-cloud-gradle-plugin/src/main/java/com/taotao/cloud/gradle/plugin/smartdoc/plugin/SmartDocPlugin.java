/*
 * smart-doc
 *
 * Copyright (C) 2018-2024 smart-doc
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.taotao.cloud.gradle.plugin.smartdoc.plugin;

import com.taotao.cloud.gradle.plugin.smartdoc.constant.GlobalConstants;
import com.taotao.cloud.gradle.plugin.smartdoc.extension.SmartDocPluginExtension;
import com.taotao.cloud.gradle.plugin.smartdoc.constant.TaskConstants;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.plugins.JavaPlugin;

/**
 * @author yu 2020/2/16.
 */
public class SmartDocPlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        project.getPluginManager().apply(JavaPlugin.class);
        Task javaCompileTask = project.getTasks().getByName(JavaPlugin.COMPILE_JAVA_TASK_NAME);
        TaskConstants.taskMap.forEach((taskName, taskClass) ->
                this.createTask(project, taskName, taskClass, javaCompileTask));

        // extend project-model to get our settings/configuration via nice configuration
        project.getExtensions().create(GlobalConstants.EXTENSION_NAME, SmartDocPluginExtension.class);
    }

    private <T extends Task> void createTask(Project project, String taskName, Class<T> taskClass, Task javaCompileTask) {
        T t = project.getTasks().create(taskName, taskClass);
        t.setGroup(GlobalConstants.TASK_GROUP);
        t.dependsOn(javaCompileTask);
    }

}
