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
package com.taotao.cloud.gradle.plugin.smartdoc.util;

import com.ly.doc.constants.DocGlobalConstants;
import com.power.common.util.CollectionUtil;
import org.gradle.api.Project;
import org.gradle.api.file.SourceDirectorySet;
import org.gradle.api.internal.tasks.DefaultSourceSetContainer;
import org.gradle.api.logging.Logger;

import java.io.File;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

/**
 * GradleSourceSetUtil
 *
 * @author 冰凝 2023-03-13 23:10
 */
public interface SourceSetUtil {

    String SOURCE_SETS = "sourceSets";
    String MAIN = "main";

    /**
     * inquire {@code SourceSet} configure the custom source code root directory
     *
     * @param project project
     * @return source code root directory
     * @implNote limited support:
     * not supported {@code SourceSet} inclusions and exclusions are configured in {@link SourceDirectorySet#getExcludes()} {@link SourceDirectorySet#getIncludes()}
     * <p>
     * if {@code SmartDoc} Support for passing file trees, which can be used directly{@link SourceDirectorySet#getFiles()}
     * @see <a href="https://docs.gradle.org/current/userguide/building_java_projects.html#sec:java_source_sets">declare source files through source sets</a>
     * @see <a href="https://docs.gradle.org/current/userguide/building_java_projects.html#sec:custom_java_source_set_paths">customize file and directory locations</a>
     */
    static Set<File> getMainJava(Project project) {
        Object sets = project.getProperties().get(SOURCE_SETS);
        if (!(sets instanceof DefaultSourceSetContainer)) {
            return Collections.emptySet();
        }
        DefaultSourceSetContainer sourceSets = (DefaultSourceSetContainer) sets;
        Logger log = project.getLogger();

        try {
            Set<File> srcDirs = sourceSets.getAt(MAIN).getJava().getSrcDirs();
            if (CollectionUtil.isEmpty(srcDirs)) {
                log.info(I18nMsgUtil.get("path_fallback_prompt"), project.getPath());
                return Collections.emptySet();
            }
            return srcDirs;
        } catch (Exception e) {
            log.warn(I18nMsgUtil.get("unexpected_errors"), e.getLocalizedMessage());
        }
        return Collections.emptySet();
    }

    /**
     * try using the default project structure: src/main/java
     *
     * @param project Project
     * @return optional default project structure
     */
    static Optional<File> getDefaultMainJava(Project project) {
        String projectDir = project.getProjectDir().getPath();
        String projectCodePath = String.join(DocGlobalConstants.FILE_SEPARATOR, projectDir, DocGlobalConstants.PROJECT_CODE_PATH);
        File src = new File(projectCodePath);

        return src.exists() && src.listFiles() != null
                ? Optional.of(src)
                : Optional.empty();
    }

}
