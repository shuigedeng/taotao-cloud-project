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
 * @author yu 2023/5/10.
 */
public class GroupIdFilterChain implements FilterChain{

    private final static Set<String> GROUPID_SET = new HashSet<>();

    static {
        GROUPID_SET.add("org.apache.commons");
        GROUPID_SET.add("io.fabric8");
        GROUPID_SET.add("io.kubernetes");
        GROUPID_SET.add("org.jooq");
        GROUPID_SET.add("org.mortbay.jetty");
        GROUPID_SET.add("com.google.http-client");
        GROUPID_SET.add("jakarta.xml.bind");
        GROUPID_SET.add("org.mariadb.jdbc");
        GROUPID_SET.add("jakarta.transaction");
        GROUPID_SET.add("jakarta.persistence");
        GROUPID_SET.add("javax.servlet");
        GROUPID_SET.add("io.projectreactor");
        GROUPID_SET.add("org.mapstruct");
        GROUPID_SET.add("io.sundr");
        GROUPID_SET.add("org.aspectj");
        GROUPID_SET.add("org.slf4j");
        GROUPID_SET.add("com.esotericsoftware.yamlbeans");
        GROUPID_SET.add("jakarta.activation");
        GROUPID_SET.add("commons-httpclient");
        GROUPID_SET.add("org.apache.curator");
        GROUPID_SET.add("org.apache.hive");
        GROUPID_SET.add("org.apache.hadoop");
        GROUPID_SET.add("org.hibernate");
        GROUPID_SET.add("org.bouncycastle");
        GROUPID_SET.add("io.vavr");
        GROUPID_SET.add("org.projectlombok");
        GROUPID_SET.add("org.freemarker");
        GROUPID_SET.add("com.auth0");
        GROUPID_SET.add("org.apache.logging.log4j");
        GROUPID_SET.add("com.google.protobuf");
        GROUPID_SET.add("org.postgresql");
        GROUPID_SET.add("com.microsoft.sqlserver");
        GROUPID_SET.add("io.etcd");
        GROUPID_SET.add("org.apache.flink");
        GROUPID_SET.add("org.apache.rocketmq");
        GROUPID_SET.add("org.apache.kafka");
        GROUPID_SET.add("org.apache.hudi");
        GROUPID_SET.add("com.rabbitmq");
        GROUPID_SET.add("org.apache.dubbo");
        GROUPID_SET.add("cn.hutool");
        GROUPID_SET.add("com.alibaba.nacos");
        GROUPID_SET.add("com.alibaba.csp");
        GROUPID_SET.add("io.zipkin.zipkin2");
        GROUPID_SET.add("org.apache.skywalking");
        GROUPID_SET.add("com.ctrip.framework.apollo");
        GROUPID_SET.add("org.apache.shardingsphere");
        GROUPID_SET.add("ru.yandex.clickhouse");
        GROUPID_SET.add("com.clickhouse");
        GROUPID_SET.add("org.apache.activemq");
        GROUPID_SET.add("org.bytedeco");
        GROUPID_SET.add("ws.schild");
        GROUPID_SET.add("io.netty");
        GROUPID_SET.add("io.micrometer");
        GROUPID_SET.add("org.apache.pulsar");
    }
    private FilterChain filterChain;
    @Override
    public void setNext(FilterChain nextInChain) {
        this.filterChain = nextInChain;
    }

    @Override
    public boolean ignoreArtifactById(CustomArtifact artifact) {
        String groupId = artifact.getGroupId();
        if (GROUPID_SET.stream().anyMatch(groupId::contains)) {
            return true;
        }
        return this.ignore(filterChain, artifact);
    }
}
