package com.taotao.cloud.ai.alibaba.vector.vector_redis.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.filter.Filter;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder;
import org.springframework.ai.vectorstore.redis.RedisVectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yingzi
 * @date 2025/4/16:17:33
 */
@RestController
@RequestMapping("/redis")
public class RedisController {

    private static final Logger logger = LoggerFactory.getLogger(RedisController.class);
    private final RedisVectorStore redisVectorStore;

    @Autowired
    public RedisController(@Qualifier("redisVectorStoreCustom") RedisVectorStore redisVectorStore) {
        this.redisVectorStore = redisVectorStore;
    }

    @GetMapping("/add")
    public void add() {
        logger.info("start add data");

        HashMap<String, Object> map = new HashMap<>();
        map.put("id", "12345");
        map.put("year", 2025);
        map.put("name", "yingzi");
        List<Document> documents = List.of(
                new Document("The World is Big and Salvation Lurks Around the Corner"),
                new Document("You walk forward facing the past and you turn back toward the future.", Map.of("year", 2024)),
                new Document("Spring AI rocks!! Spring AI rocks!! Spring AI rocks!! Spring AI rocks!! Spring AI rocks!!", map));
        redisVectorStore.add(documents);
    }

    @GetMapping("/search")
    public List<Document> search() {
        logger.info("start search data");
        return redisVectorStore.similaritySearch(SearchRequest
                .builder()
                .query("Spring")
                .topK(2)
                .build());
    }

    @GetMapping("delete-filter")
    public void deleteFilter() {
        logger.info("start delete data with filter");
        FilterExpressionBuilder b = new FilterExpressionBuilder();
        Filter.Expression expression = b.and(
                b.gte("year", 2024),
                b.eq("name", "yingzi")
        ).build();

        redisVectorStore.delete(expression);
    }
}
