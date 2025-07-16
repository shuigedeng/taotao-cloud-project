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

package com.taotao.cloud.ai.alibaba.vector.vector_redis.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.TokenCountBatchingStrategy;
import org.springframework.ai.vectorstore.redis.RedisVectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPooled;

/**
 * @author yingzi
 * @date 2025/5/29 11:32
 */
@Configuration
public class RedisConfig {

    private static final Logger logger = LoggerFactory.getLogger(RedisConfig.class);

    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private int port;

    @Value("${spring.ai.vectorstore.redis.prefix}")
    private String prefix;

    @Value("${spring.ai.vectorstore.redis.index-name}")
    private String indexName;

    @Bean
    public JedisPooled jedisPooled() {
        logger.info("Redis host: {}, port: {}", host, port);
        return new JedisPooled(host, port);
    }

    @Bean
    @Qualifier("redisVectorStoreCustom")
    public RedisVectorStore vectorStore(JedisPooled jedisPooled, EmbeddingModel embeddingModel) {
        logger.info("create redis vector store");
        return RedisVectorStore.builder(jedisPooled, embeddingModel)
                .indexName(indexName) // Optional: defaults to "spring-ai-index"
                .prefix(prefix) // Optional: defaults to "embedding:"
                .metadataFields( // Optional: define metadata fields for filtering
                        RedisVectorStore.MetadataField.tag("name"),
                        RedisVectorStore.MetadataField.numeric("year"))
                .initializeSchema(true) // Optional: defaults to false
                .batchingStrategy(new TokenCountBatchingStrategy()) // Optional: defaults to
                // TokenCountBatchingStrategy
                .build();
    }
}
