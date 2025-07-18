package com.taotao.cloud.ai.alibaba.rag.rag_simple.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yingzi
 * @date 2025/5/26 21:35
 */
@RestController
@RequestMapping("/rag/simple")
public class RagSimpleController {

    private static final Logger logger = LoggerFactory.getLogger(RagSimpleController.class);
    private final SimpleVectorStore simpleVectorStore;
    private final ChatClient chatClient;

    public RagSimpleController(EmbeddingModel embeddingModel, ChatClient.Builder builder) {
        this.simpleVectorStore = SimpleVectorStore
                .builder(embeddingModel).build();
        this.chatClient = builder.build();
    }

    @GetMapping("/add")
    public void add() {
        logger.info("start add data");
        HashMap<String, Object> map = new HashMap<>();
        map.put("year", 2025);
        map.put("name", "yingzi");
        List<Document> documents = List.of(
                new Document("你的姓名是影子，湖南邵阳人，25年硕士毕业于北京科技大学，曾先后在百度、理想、快手实习，曾发表过一篇自然语言处理的sci，现在是一名AI研发工程师"),
                new Document("你的姓名是影子，专业领域包含的数学、前后端、大数据、自然语言处理", Map.of("year", 2024)),
                new Document("你姓名是影子，爱好是发呆、思考、运动", map));
        simpleVectorStore.add(documents);
    }

    @GetMapping("/chat")
    public String chat(@RequestParam(value = "query", defaultValue = "你好，请告诉我影子这个人的身份信息") String query) {
        logger.info("start chat");
        return chatClient.prompt(query).call().content();
    }

    @GetMapping("/chat-rag-advisor")
    public String chatRagAdvisor(@RequestParam(value = "query", defaultValue = "你好，请告诉我影子这个人的身份信息") String query) {
        logger.info("start chat with rag-advisor");
        RetrievalAugmentationAdvisor retrievalAugmentationAdvisor = RetrievalAugmentationAdvisor.builder()
                .documentRetriever(VectorStoreDocumentRetriever.builder()
                        .vectorStore(simpleVectorStore)
                        .build())
                .build();

        return chatClient.prompt(query)
                .advisors(retrievalAugmentationAdvisor)
                .call().content();
    }
}
