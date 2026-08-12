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
package com.taotao.cloud.gradle.plugin.smartdoc.constant;

import com.taotao.cloud.gradle.plugin.smartdoc.constant.GlobalConstants;
import com.taotao.cloud.gradle.plugin.smartdoc.task.*;
import com.taotao.cloud.gradle.plugin.smartdoc.task.DocBaseTask;
import com.taotao.cloud.gradle.plugin.smartdoc.task.GrpcAdocTask;
import com.taotao.cloud.gradle.plugin.smartdoc.task.GrpcHtmlTask;
import com.taotao.cloud.gradle.plugin.smartdoc.task.GrpcMarkdownTask;
import com.taotao.cloud.gradle.plugin.smartdoc.task.JMeterTask;
import com.taotao.cloud.gradle.plugin.smartdoc.task.JavadocAdocTask;
import com.taotao.cloud.gradle.plugin.smartdoc.task.JavadocHtmlTask;
import com.taotao.cloud.gradle.plugin.smartdoc.task.JavadocMarkdownTask;
import com.taotao.cloud.gradle.plugin.smartdoc.task.OpenApiTask;
import com.taotao.cloud.gradle.plugin.smartdoc.task.PostmanTask;
import com.taotao.cloud.gradle.plugin.smartdoc.task.RestAdocTask;
import com.taotao.cloud.gradle.plugin.smartdoc.task.RestHtmlTask;
import com.taotao.cloud.gradle.plugin.smartdoc.task.RestMarkdownTask;
import com.taotao.cloud.gradle.plugin.smartdoc.task.RpcAdocTask;
import com.taotao.cloud.gradle.plugin.smartdoc.task.RpcHtmlTask;
import com.taotao.cloud.gradle.plugin.smartdoc.task.RpcMarkdownTask;
import com.taotao.cloud.gradle.plugin.smartdoc.task.SwaggerTask;
import com.taotao.cloud.gradle.plugin.smartdoc.task.TornaRestTask;
import com.taotao.cloud.gradle.plugin.smartdoc.task.TornaRpcTask;
import com.taotao.cloud.gradle.plugin.smartdoc.task.WebSocketAdocTask;
import com.taotao.cloud.gradle.plugin.smartdoc.task.WebSocketHtmlTask;
import com.taotao.cloud.gradle.plugin.smartdoc.task.WebSocketMarkdownTask;
import com.taotao.cloud.gradle.plugin.smartdoc.task.WordTask;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yu 2020/11/23.
 */
public class TaskConstants {

    /**
     * Map of Gradle Task
     */
    public static Map<String, Class<? extends DocBaseTask>> taskMap = new HashMap<>();

    static {
        // create html
        taskMap.put(GlobalConstants.REST_HTML_TASK, RestHtmlTask.class);
        // create adoc
        taskMap.put(GlobalConstants.REST_ADOC_TASK, RestAdocTask.class);
        // create markdown
        taskMap.put(GlobalConstants.REST_MARKDOWN_TASK, RestMarkdownTask.class);
        // create jmeter
        taskMap.put(GlobalConstants.JMETER_TASK, JMeterTask.class);
        // create postman collection
        taskMap.put(GlobalConstants.POSTMAN_TASK, PostmanTask.class);
        // create open api
        taskMap.put(GlobalConstants.OPEN_API_TASK, OpenApiTask.class);
        // create rpc html
        taskMap.put(GlobalConstants.RPC_HTML_TASK, RpcHtmlTask.class);
        // create rpc adoc
        taskMap.put(GlobalConstants.RPC_ADOC_TASK, RpcAdocTask.class);
        // create rpc Markdown
        taskMap.put(GlobalConstants.RPC_MARKDOWN_TASK, RpcMarkdownTask.class);
        // create torna rest
        taskMap.put(GlobalConstants.TORNA_REST_TASK, TornaRestTask.class);
        // create torna rpc
        taskMap.put(GlobalConstants.TORNA_RPC_TASK, TornaRpcTask.class);
        // create word rest
        taskMap.put(GlobalConstants.WORD_TASK, WordTask.class);
        // create Swagger
        taskMap.put(GlobalConstants.SWAGGER_TASK, SwaggerTask.class);
        // create websocket html
        taskMap.put(GlobalConstants.WEBSOCKET_HTML_TASK, WebSocketHtmlTask.class);
        // create websocket adoc
        taskMap.put(GlobalConstants.WEBSOCKET_ADOC_TASK, WebSocketAdocTask.class);
        // create websocket markdown
        taskMap.put(GlobalConstants.WEBSOCKET_MARKDOWN_TASK, WebSocketMarkdownTask.class);
        // create javadoc html
        taskMap.put(GlobalConstants.JAVADOC_HTML_TASK, JavadocHtmlTask.class);
        // create javadoc adoc
        taskMap.put(GlobalConstants.JAVADOC_ADOC_TASK, JavadocAdocTask.class);
        // create javadoc markdown
        taskMap.put(GlobalConstants.JAVADOC_MARKDOWN_TASK, JavadocMarkdownTask.class);
        // create gRPC html
        taskMap.put(GlobalConstants.GRPC_HTML_TASK, GrpcHtmlTask.class);
        // create gRPC adoc
        taskMap.put(GlobalConstants.GRPC_ADOC_TASK, GrpcAdocTask.class);
        // create gRPC markdown
        taskMap.put(GlobalConstants.GRPC_MARKDOWN_TASK, GrpcMarkdownTask.class);

    }
}
