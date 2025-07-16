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

package com.taotao.cloud.ai.alibaba.rag.rag_elasticsearch.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.generation.augmentation.ContextualQueryAugmenter;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.elasticsearch.ElasticsearchVectorStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yingzi
 * @date 2025/5/26 22:39
 */
@RestController
@RequestMapping("/rag/es")
public class RagEsController {

    private static final Logger logger = LoggerFactory.getLogger(RagEsController.class);

    private final ElasticsearchVectorStore elasticsearchVectorStore;
    private final ChatClient chatClient;

    public RagEsController(
            ChatClient.Builder builder, ElasticsearchVectorStore elasticsearchVectorStore) {
        this.elasticsearchVectorStore = elasticsearchVectorStore;
        this.chatClient = builder.build();
    }

    @RequestMapping("/add")
    public void add() {
        logger.info("start add data");
        HashMap<String, Object> map = new HashMap<>();
        map.put("year", 2025);
        map.put("name", "yingzi");
        List<Document> documents =
                List.of(
                        new Document(
                                "你的姓名是影子，湖南邵阳人，25年硕士毕业于北京科技大学，曾先后在百度、理想、快手实习，曾发表过一篇自然语言处理的sci，现在是一名AI研发工程师"),
                        new Document("你的姓名是影子，专业领域包含的数学、前后端、大数据、自然语言处理", Map.of("year", 2024)),
                        new Document("你姓名是影子，爱好是发呆、思考、运动", map));
        elasticsearchVectorStore.add(documents);
    }

    @GetMapping("/chat-rag-advisor")
    public String chatRagAdvisor(
            @RequestParam(value = "query", defaultValue = "你好，请告诉我影子这个人的身份信息") String query) {
        logger.info("start chat with rag-advisor");
        RetrievalAugmentationAdvisor retrievalAugmentationAdvisor =
                RetrievalAugmentationAdvisor.builder()
                        .documentRetriever(
                                VectorStoreDocumentRetriever.builder()
                                        .vectorStore(elasticsearchVectorStore)
                                        .build())
                        .queryAugmenter(
                                ContextualQueryAugmenter.builder().allowEmptyContext(true).build())
                        .build();

        return chatClient.prompt(query).advisors(retrievalAugmentationAdvisor).call().content();
    }
}
