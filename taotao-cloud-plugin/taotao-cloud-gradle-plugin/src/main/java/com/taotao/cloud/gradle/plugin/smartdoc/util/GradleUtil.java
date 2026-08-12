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

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.taotao.cloud.gradle.plugin.smartdoc.util.ClassLoaderUtil;
import com.ly.doc.model.*;
import com.power.common.util.FileUtil;
import com.power.common.util.StringUtil;
import org.gradle.api.Project;
import org.gradle.api.logging.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Objects;

/**
 * @author yu 2020/2/16.
 */
public class GradleUtil {
    /**
     * Gson Object
     */
    public final static Gson GSON = new GsonBuilder().addDeserializationExclusionStrategy(new ExclusionStrategy() {
        @Override
        public boolean shouldSkipField(FieldAttributes fieldAttributes) {
            return false;
        }

        @Override
        public boolean shouldSkipClass(Class<?> aClass) {
            return false;
        }
    }).create();

    /**
     * Build ApiConfig
     *
     * @param configFile config file
     * @param project    Project object
     * @param increment  increment
     * @param log        gradle plugin log
     * @return com.power.doc.model.ApiConfig
     */
    public static ApiConfig buildConfig(File configFile, Project project, Boolean increment, Logger log) {
        try {
            ClassLoader classLoader = ClassLoaderUtil.getRuntimeClassLoader(project);
            String data = FileUtil.getFileContent(new FileInputStream(configFile));
            ApiConfig apiConfig = GSON.fromJson(data, ApiConfig.class);
            apiConfig.setClassLoader(classLoader);
            List<ApiDataDictionary> apiDataDictionaries = apiConfig.getDataDictionaries();
            List<ApiErrorCodeDictionary> apiErrorCodes = apiConfig.getErrorCodeDictionaries();
            List<ApiConstant> apiConstants = apiConfig.getApiConstants();
            BodyAdvice responseBodyAdvice = apiConfig.getResponseBodyAdvice();
            BodyAdvice requestBodyAdvice = apiConfig.getRequestBodyAdvice();
            if (Objects.nonNull(apiErrorCodes)) {
                apiErrorCodes.forEach(
                        apiErrorCode -> {
                            String className = apiErrorCode.getEnumClassName();
                            apiErrorCode.setEnumClass(getClassByClassName(className, classLoader));
                        }
                );
            }
            if (Objects.nonNull(apiDataDictionaries)) {
                apiDataDictionaries.forEach(
                        apiDataDictionary -> {
                            String className = apiDataDictionary.getEnumClassName();
                            apiDataDictionary.setEnumClass(getClassByClassName(className, classLoader));
                        }
                );
            }
            if (Objects.nonNull(apiConstants)) {
                apiConstants.forEach(
                        apiConstant -> {
                            String className = apiConstant.getConstantsClassName();
                            apiConstant.setConstantsClass(getClassByClassName(className, classLoader));
                        }
                );
            }
            if (Objects.nonNull(responseBodyAdvice) && StringUtil.isNotEmpty(responseBodyAdvice.getClassName())) {
                responseBodyAdvice.setWrapperClass(getClassByClassName(responseBodyAdvice.getClassName(), classLoader));
            }
            if (Objects.nonNull(requestBodyAdvice) && StringUtil.isNotEmpty(requestBodyAdvice.getClassName())) {
                requestBodyAdvice.setWrapperClass(getClassByClassName(requestBodyAdvice.getClassName(), classLoader));
            }

            if (StringUtil.isEmpty(apiConfig.getProjectName())) {
                apiConfig.setProjectName(project.getName());
            }
            addSourcePaths(project, apiConfig, log);
            if (Objects.nonNull(increment)) {
                // overwrite by plugin
                apiConfig.setIncrement(increment);
            }

            apiConfig.setBaseDir(project.getProjectDir().getAbsolutePath());

            return apiConfig;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * Get Class by name
     *
     * @param className   class name
     * @param classLoader urls
     * @return Class
     */
    public static Class getClassByClassName(String className, ClassLoader classLoader) {
        try {
            return classLoader.loadClass(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void addSourcePaths(Project project, ApiConfig apiConfig, Logger log) {
        // do nothing
    }
}
