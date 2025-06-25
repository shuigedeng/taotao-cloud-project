package com.taotao.cloud.ai.alibaba.vector.vector_elasticsearch.config;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.TokenCountBatchingStrategy;
import org.springframework.ai.vectorstore.elasticsearch.ElasticsearchVectorStore;
import org.springframework.ai.vectorstore.elasticsearch.ElasticsearchVectorStoreOptions;
import org.springframework.ai.vectorstore.elasticsearch.SimilarityFunction;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author yingzi
 * @date 2025/4/14:22:48
 */
@Configuration
public class ElasticsearchConfig {

    private static final Logger logger = LoggerFactory.getLogger(ElasticsearchConfig.class);


    @Value("${spring.elasticsearch.uris}")
    private String url;
    @Value("${spring.elasticsearch.username}")
    private String username;
    @Value("${spring.elasticsearch.password}")
    private String password;

    @Value("${spring.ai.vectorstore.elasticsearch.index-name}")
    private String indexName;
    @Value("${spring.ai.vectorstore.elasticsearch.similarity}")
    private SimilarityFunction similarityFunction;
    @Value("${spring.ai.vectorstore.elasticsearch.dimensions}")
    private int dimensions;


    @Bean
    public RestClient restClient() {
        // 解析URL
        String[] urlParts = url.split("://");
        String protocol = urlParts[0];
        String hostAndPort = urlParts[1];
        String[] hostPortParts = hostAndPort.split(":");
        String host = hostPortParts[0];
        int port = Integer.parseInt(hostPortParts[1]);

        // 创建凭证提供者
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, 
            new UsernamePasswordCredentials(username, password));

        logger.info("create elasticsearch rest client");
        // 构建RestClient
        return RestClient.builder(new HttpHost(host, port, protocol))
                .setHttpClientConfigCallback(httpClientBuilder -> {
                    httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
                    return httpClientBuilder;
                })
                .build();
    }

    @Bean
    @Qualifier("elasticsearchVectorStore")
    public ElasticsearchVectorStore vectorStore(RestClient restClient, EmbeddingModel embeddingModel) {
        logger.info("create elasticsearch vector store");

        ElasticsearchVectorStoreOptions options = new ElasticsearchVectorStoreOptions();
        options.setIndexName(indexName);    // Optional: defaults to "spring-ai-document-index"
        options.setSimilarity(similarityFunction);           // Optional: defaults to COSINE
        options.setDimensions(dimensions);             // Optional: defaults to model dimensions or 1536

        return ElasticsearchVectorStore.builder(restClient, embeddingModel)
                .options(options)                     // Optional: use custom options
                .initializeSchema(true)               // Optional: defaults to false
                .batchingStrategy(new TokenCountBatchingStrategy())// Optional: defaults to TokenCountBatchingStrategy
                .build();
    }

}
