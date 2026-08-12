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

import com.taotao.cloud.gradle.plugin.smartdoc.chain.*;
import com.taotao.cloud.gradle.plugin.smartdoc.model.CustomArtifact;


/**
 * Artifact filter util
 *
 * @author yu 2020/1/11.
 */
public class ArtifactFilterUtil {

    /**
     * ignoreArtifact
     *
     * @param artifact Artifact
     * @return boolean
     */
    public static boolean ignoreArtifact(CustomArtifact artifact) {
        FilterChain groupFilterChain = new GroupIdFilterChain();
        FilterChain startsWithFilterChain = new StartsWithFilterChain();
        FilterChain containsFilterChain = new ContainsFilterChain();
        FilterChain commonArtifactFilterChain = new CommonArtifactFilterChain();
        FilterChain springBootArtifactFilterChain = new SpringBootArtifactFilterChain();

        groupFilterChain.setNext(startsWithFilterChain);
        startsWithFilterChain.setNext(containsFilterChain);
        containsFilterChain.setNext(commonArtifactFilterChain);
        commonArtifactFilterChain.setNext(springBootArtifactFilterChain);
        return groupFilterChain.ignoreArtifactById(artifact);
    }

    /**
     * Ignore Spring Boot Artifact
     * @param artifact Artifact
     * @return boolean
     */
    public static boolean ignoreSpringBootArtifactById(CustomArtifact artifact) {
        FilterChain springBootArtifactFilterChain = new SpringBootArtifactFilterChain();
        return springBootArtifactFilterChain.ignoreArtifactById(artifact);
    }
}
