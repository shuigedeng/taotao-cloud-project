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
package com.taotao.cloud.gradle.plugin.smartdoc.chain;

import com.taotao.cloud.gradle.plugin.smartdoc.model.CustomArtifact;

/**
 * @author yu 2020/1/13.
 */
public class SpringBootArtifactFilterChain implements FilterChain {

    private FilterChain filterChain;

    @Override
    public void setNext(FilterChain nextInChain) {
        this.filterChain = nextInChain;
    }

    @Override
    public boolean ignoreArtifactById(CustomArtifact artifact) {
        String artifactId = artifact.getArtifactId();
        switch (artifactId) {
            case "spring-boot":
            case "spring-boot-starter-actuator":
            case "spring-boot-starter":
            case "spring-boot-starter-undertow":
            case "spring-boot-starter-aop":
            case "spring-boot-starter-json":
            case "spring-boot-starter-web":
            case "spring-boot-starter-logging":
            case "spring-boot-starter-tomcat":
            case "spring-boot-starter-validation":
            case "spring-boot-starter-security":
            case "spring-boot-starter-data-redis":
            case "spring-boot-starter-activemq":
            case "spring-boot-starter-log4j2":
            case "spring-boot-actuator-autoconfigure":
            case "spring-boot-starter-oauth2-client":
            case "spring-boot-starter-quartz":
            case "spring-boot-starter-batch":
            case "spring-boot-starter-jdbc":
            case "spring-cloud-starter":
                return true;
            default:
                return this.ignore(filterChain, artifact);
        }
    }
}
