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
package com.taotao.cloud.gradle.plugin.smartdoc.model;

import java.util.Objects;

/**
 * @author yu 2020/4/19.
 */
public class CustomArtifact {

    /**
     * Artifact ID
     */
    private String artifactId;

    /**
     * Artifact Group
     */
    private String groupId;

    /**
     * Artifact Version
     */
    private String version;

    /**
     * Build CustomArtifact
     * @return CustomArtifact
     */
    public static CustomArtifact builder() {
        return new CustomArtifact();
    }

    /**
     * Build CustomArtifact with ArtifactDisplayName
     * @param artifactDisplayName Artifact Display Name
     * @return CustomArtifact
     */
    public static CustomArtifact builder(String artifactDisplayName) {
        CustomArtifact artifact = builder();
        if (Objects.isNull(artifactDisplayName)) {
            return artifact;
        }
        String[] displayInfo = artifactDisplayName.split(":");
        artifact.setArtifactId(displayInfo[1]);
        artifact.setGroup(displayInfo[0]);
        if (displayInfo.length > 2) {
            artifact.setVersion(displayInfo[2]);
        }
        return artifact;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public CustomArtifact setArtifactId(String artifactId) {
        this.artifactId = artifactId;
        return this;
    }

    public String getGroupId() {
        return groupId;
    }

    public CustomArtifact setGroup(String group) {
        this.groupId = group;
        return this;
    }

    public String getVersion() {
        return version;
    }

    public CustomArtifact setVersion(String version) {
        this.version = version;
        return this;
    }
}
