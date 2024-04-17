package com.taotao.cloud.ai.config;

import org.springframework.ai.embedding.EmbeddingClient;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.PgVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * 将PDF内容向量化并保存在向量数据库中，本Demo使用的是PGVector；
 * 调用对话前根据问题从向量数据库中检索最相似的几条记录；
 * 封装数据，一并返回给大语言模型
 * 大语言模型根据上下文数据进行回复
 */
@Configuration
public class ApplicationConfig {

	/**
	 * 向量数据库进行检索操作
	 *
	 * @param embeddingClient
	 * @param jdbcTemplate
	 * @return
	 */
	@Bean
	public VectorStore vectorStore(EmbeddingClient embeddingClient, JdbcTemplate jdbcTemplate) {
		return new PgVectorStore(jdbcTemplate, embeddingClient);
	}

	/**
	 * 文本分割器
	 *
	 * @return
	 */
	@Bean
	public TokenTextSplitter tokenTextSplitter() {
		return new TokenTextSplitter();
	}
}
