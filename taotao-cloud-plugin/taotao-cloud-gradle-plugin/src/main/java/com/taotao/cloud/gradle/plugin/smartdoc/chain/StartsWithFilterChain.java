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

import java.util.HashSet;
import java.util.Set;

/**
 * @author yu 2020/1/13.
 */
public class StartsWithFilterChain implements FilterChain {

    private final static Set<String> PREFIX_SET = new HashSet<>();

    static {
        PREFIX_SET.add("maven");
        PREFIX_SET.add("asm");
        PREFIX_SET.add("tomcat");
        PREFIX_SET.add("jboss");
        PREFIX_SET.add("undertow");
        PREFIX_SET.add("jackson");
        PREFIX_SET.add("micrometer");
        PREFIX_SET.add("spring-boot-actuator");
        PREFIX_SET.add("sharding");
        PREFIX_SET.add("mybatis-spring-boot-starter");
        PREFIX_SET.add("flexmark");
        PREFIX_SET.add("netty");
        PREFIX_SET.add("hibernate-core");
        PREFIX_SET.add("springdoc-openapi");
        PREFIX_SET.add("poi");
        PREFIX_SET.add("commons-io");
        PREFIX_SET.add("commons-lang");
        PREFIX_SET.add("commons-logging");
        PREFIX_SET.add("jaxb");
        PREFIX_SET.add("byte-buddy");
        PREFIX_SET.add("rxjava");
        PREFIX_SET.add("kotlin");
        PREFIX_SET.add("checker-qual");
        PREFIX_SET.add("nacos");
        PREFIX_SET.add("junit");
        PREFIX_SET.add("caffeine");
        PREFIX_SET.add("lettuce-core");
        PREFIX_SET.add("spring-oxm");
        PREFIX_SET.add("spring-data-redis");
        PREFIX_SET.add("json");
        PREFIX_SET.add("springfox");
        PREFIX_SET.add("elasticsearch");
        PREFIX_SET.add("guava");
        PREFIX_SET.add("fastjson");
        PREFIX_SET.add("bcprov");
        PREFIX_SET.add("aws-java-sdk");
        PREFIX_SET.add("hadoop");
        PREFIX_SET.add("xml");
        PREFIX_SET.add("sundr-codegen");
    }

    private FilterChain filterChain;

    @Override
    public void setNext(FilterChain nextInChain) {
        this.filterChain = nextInChain;
    }

    @Override
    public boolean ignoreArtifactById(CustomArtifact artifact) {
        String artifactId = artifact.getArtifactId();
        if (PREFIX_SET.stream().anyMatch(artifactId::startsWith)) {
            return true;
        }
        return this.ignore(filterChain, artifact);
    }
}
