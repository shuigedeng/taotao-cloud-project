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

/**
 * @author yu 2019/12/13.
 */
public interface GlobalConstants {

    /**
     * error message
     */
    String ERROR_MSG = "Failed to build ApiConfig, check if the configuration file is correct.";

    /**
     * default config file
     */
    String DEFAULT_CONFIG = "./src/main/resources/default.json";

    /**
     * Task group
     */
    String TASK_GROUP = "Documentation";

    /**
     * Generate Rest html document
     */
    String REST_HTML_TASK = "smartDocRestHtml";

    /**
     * Generate Rest Adoc document
     */
    String REST_ADOC_TASK = "smartDocRestAdoc";

    /**
     * Generate Rest markdown document
     */
    String REST_MARKDOWN_TASK = "smartDocRestMarkdown";

    /**
     * Generate JMeter test document
     */
    String JMETER_TASK = "smartDocJmeter";

    /**
     * Generate Postman document
     */
    String POSTMAN_TASK = "smartDocPostman";

    /**
     * Generate OpenAPI document
     */
    String OPEN_API_TASK = "smartDocOpenApi";

    /**
     * Generate Rpc html document
     */
    String RPC_HTML_TASK = "smartDocRpcHtml";

    /**
     * Generate Rpc adoc document
     */
    String RPC_ADOC_TASK = "smartDocRpcAdoc";

    /**
     * Generate Rpc markdown document
     */
    String RPC_MARKDOWN_TASK = "smartDocRpcMarkdown";

    /**
     * Generate rest document push to torna
     */
    String TORNA_REST_TASK = "tornaRest";

    /**
     * Generate rpc document push to torna
     */
    String TORNA_RPC_TASK = "tornaRpc";
    /**
     * Generate rest document push to word
     */
    String WORD_TASK = "word";

    /**
     * Generate Swagger document
     */
    String SWAGGER_TASK = "swagger";

    /**
     * Generate WebSocket markdown document
     */
    String WEBSOCKET_MARKDOWN_TASK = "webSocketMarkdown";

    /**
     * Generate WebSocket html document
     */
    String WEBSOCKET_HTML_TASK = "webSocketHtml";

    /**
     * Generate WebSocket ascii document
     */
    String WEBSOCKET_ADOC_TASK = "webSocketAdoc";

    /**
     * Generate JavaDoc html document
     */
    String JAVADOC_HTML_TASK = "javadocHtml";

    /**
     * Generate JavaDoc adoc document
     */
    String JAVADOC_ADOC_TASK = "javadocAdoc";

    /**
     * Generate JavaDoc markdown document
     */
    String JAVADOC_MARKDOWN_TASK = "javadocMarkdown";

    /**
     * Generate gRPC html document
     */
    String GRPC_HTML_TASK = "gRPCHtml";

    /**
     * Generate gRPC adoc document
     */
    String GRPC_ADOC_TASK = "gRPCAdoc";

    /**
     * Generate gRPC markdown document
     */
    String GRPC_MARKDOWN_TASK = "gRPCMarkdown";

    /**
     * Plugin extension name
     */
    String EXTENSION_NAME = "smartdoc";

    /**
     * default java source dir
     */
    String SRC_MAIN_JAVA_PATH = "src/main/java";

    String CONFIG_FILE = "smartdoc.configFile";

}
