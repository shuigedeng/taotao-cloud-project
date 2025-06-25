package com.taotao.cloud.ai.alibaba.rag.rag_simple.controller;

import com.spring.ai.tutorial.rag.service.DocumentSelectFirst;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.generation.augmentation.ContextualQueryAugmenter;
import org.springframework.ai.rag.preretrieval.query.expansion.MultiQueryExpander;
import org.springframework.ai.rag.preretrieval.query.transformation.TranslationQueryTransformer;
import org.springframework.ai.rag.retrieval.join.ConcatenationDocumentJoiner;
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
 * @date 2025/5/29 18:27
 */
@RestController
@RequestMapping("/rag/module")
public class RagModuleController {

    private static final Logger logger = LoggerFactory.getLogger(RagSimpleController.class);
    private final SimpleVectorStore simpleVectorStore;
    private final ChatClient.Builder chatClientBuilder;

    public RagModuleController(EmbeddingModel embeddingModel, ChatClient.Builder builder) {
        this.simpleVectorStore = SimpleVectorStore
                .builder(embeddingModel).build();
        this.chatClientBuilder = builder;
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

    @GetMapping("/chat-rag-advisor")
    public String chatRagAdvisor(@RequestParam(value = "query", defaultValue = "你好，请告诉我影子这个人的身份信息") String query) {
        logger.info("start chat with rag-advisor");

        // 1. Pre-Retrieval
            // 1.1 MultiQueryExpander
        MultiQueryExpander multiQueryExpander = MultiQueryExpander.builder()
                .chatClientBuilder(this.chatClientBuilder)
                .build();
            // 1.2 TranslationQueryTransformer
        TranslationQueryTransformer translationQueryTransformer = TranslationQueryTransformer.builder()
                .chatClientBuilder(this.chatClientBuilder)
                .targetLanguage("English")
                .build();

        // 2. Retrieval
            // 2.1 VectorStoreDocumentRetriever
        VectorStoreDocumentRetriever vectorStoreDocumentRetriever = VectorStoreDocumentRetriever.builder()
                .vectorStore(simpleVectorStore)
                .build();
        // 2.2 ConcatenationDocumentJoiner
        ConcatenationDocumentJoiner concatenationDocumentJoiner = new ConcatenationDocumentJoiner();

        // 3. Post-Retrieval
            // 3.1 DocumentSelectFirst
        DocumentSelectFirst documentSelectFirst = new DocumentSelectFirst();

        // 4. Generation
            // 4.1 ContextualQueryAugmenter
        ContextualQueryAugmenter contextualQueryAugmenter = ContextualQueryAugmenter.builder()
                .allowEmptyContext(true)
                .build();

        RetrievalAugmentationAdvisor retrievalAugmentationAdvisor = RetrievalAugmentationAdvisor.builder()
                // 扩充为原来的3倍
                .queryExpander(multiQueryExpander)
                // 转为英文
                .queryTransformers(translationQueryTransformer)

                // 从向量存储中检索文档
                .documentRetriever(vectorStoreDocumentRetriever)
                // 将检索到的文档进行拼接
                .documentJoiner(concatenationDocumentJoiner)

                // 对检索到的文档进行处理，选择第一个
                .documentPostProcessors(documentSelectFirst)

                // 对生成的查询进行上下文增强
                .queryAugmenter(contextualQueryAugmenter)
                .build();

        return this.chatClientBuilder.build().prompt(query)
                .advisors(retrievalAugmentationAdvisor)
                .call().content();
    }
}
