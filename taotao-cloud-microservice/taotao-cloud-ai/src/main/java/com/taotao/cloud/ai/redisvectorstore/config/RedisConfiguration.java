package com.taotao.cloud.ai.redisvectorstore.config;

import org.springframework.ai.autoconfigure.vectorstore.redis.RedisVectorStoreProperties;
import org.springframework.ai.document.MetadataMode;
import org.springframework.ai.transformers.TransformersEmbeddingModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.redis.RedisVectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPooled;

@Configuration
public class RedisConfiguration {

	@Bean
	TransformersEmbeddingModel transformersEmbeddingClient() {
		return new TransformersEmbeddingModel(MetadataMode.EMBED);
	}

	@Bean
	VectorStore vectorStore(TransformersEmbeddingModel embeddingClient, RedisVectorStoreProperties properties) {

		RedisVectorStore vectorStore = RedisVectorStore.builder(new JedisPooled(), embeddingClient).indexName(properties.getIndex()).prefix(properties.getPrefix()).build();
		vectorStore.afterPropertiesSet();
		return vectorStore;
	}


}
