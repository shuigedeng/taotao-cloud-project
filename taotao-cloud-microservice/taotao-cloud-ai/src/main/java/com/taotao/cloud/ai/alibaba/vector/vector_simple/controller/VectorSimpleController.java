/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.ai.alibaba.vector.vector_simple.controller;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.filter.Filter;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yingzi
 * @date 2025/5/26 20:26
 */
@RestController
@RequestMapping("/vector/simple")
public class VectorSimpleController {
    private static final Logger logger = LoggerFactory.getLogger(VectorSimpleController.class);
    private final SimpleVectorStore simpleVectorStore;
    private final String SAVE_PATH =
            System.getProperty("user.dir") + "/vector/vector-simple/src/main/resources/save.json";

    public VectorSimpleController(EmbeddingModel embeddingModel) {
        this.simpleVectorStore = SimpleVectorStore.builder(embeddingModel).build();
    }

    @GetMapping("/add")
    public void add() {
        logger.info("start add data");

        HashMap<String, Object> map = new HashMap<>();
        map.put("year", 2025);
        map.put("name", "yingzi");
        List<Document> documents =
                List.of(
                        new Document("The World is Big and Salvation Lurks Around the Corner"),
                        new Document(
                                "You walk forward facing the past and you turn back toward the future.",
                                Map.of("year", 2024)),
                        new Document(
                                "Spring AI rocks!! Spring AI rocks!! Spring AI rocks!! Spring AI rocks!! Spring AI rocks!!",
                                map),
                        new Document("1", "test content", map));
        simpleVectorStore.add(documents);
    }

    @GetMapping("/delete")
    public void delete() {
        logger.info("start delete data");
        simpleVectorStore.delete(List.of("1"));
    }

    @GetMapping("/save")
    public void save() {
        logger.info("start save data: {}", SAVE_PATH);
        File file = new File(SAVE_PATH);
        if (file.exists()) {
            file.delete();
        }
        simpleVectorStore.save(file);
    }

    @GetMapping("/load")
    public void load() {
        logger.info("start load data: {}", SAVE_PATH);
        File file = new File(SAVE_PATH);
        simpleVectorStore.load(file);
    }

    @GetMapping("/search")
    public List<Document> search() {
        logger.info("start search data");
        return simpleVectorStore.similaritySearch(
                SearchRequest.builder().query("Spring").topK(2).build());
    }

    @GetMapping("/search-filter")
    public List<Document> searchFilter() {
        logger.info("start search  filter data");
        FilterExpressionBuilder b = new FilterExpressionBuilder();
        Filter.Expression expression =
                b.and(b.in("year", 2025, 2024), b.eq("name", "yingzi")).build();

        return simpleVectorStore.similaritySearch(
                SearchRequest.builder()
                        .query("Spring")
                        .topK(2)
                        .filterExpression(expression)
                        .build());
    }
}
