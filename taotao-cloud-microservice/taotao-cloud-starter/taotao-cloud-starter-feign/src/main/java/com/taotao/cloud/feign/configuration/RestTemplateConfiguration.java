package com.taotao.cloud.feign.configuration;


import com.alibaba.cloud.sentinel.annotation.SentinelRestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.cloud.feign.http.InfoFeignLoggerFactory;
import com.taotao.cloud.feign.http.RestTemplateHeaderInterceptor;
import feign.Logger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.commons.httpclient.OkHttpClientConnectionPoolFactory;
import org.springframework.cloud.commons.httpclient.OkHttpClientFactory;
import org.springframework.cloud.openfeign.FeignLoggerFactory;
import org.springframework.cloud.openfeign.support.FeignHttpClientProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Collections;
import java.util.List;

/**
 * RestTemplate 相关的配置
 *
 * @author zuihou
 */
@ConditionalOnClass(okhttp3.OkHttpClient.class)
public class RestTemplateConfiguration {

    private static final Charset UTF_8 = StandardCharsets.UTF_8;
    private final ObjectMapper objectMapper;

	public RestTemplateConfiguration(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	@Bean
    @ConditionalOnMissingBean(FeignLoggerFactory.class)
    public FeignLoggerFactory getInfoFeignLoggerFactory() {
        return new InfoFeignLoggerFactory();
    }

    @Bean
    @Profile({"dev", "test"})
    Logger.Level devFeignLoggerLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    @Profile({"docker", "uat", "prod"})
    Logger.Level prodFeignLoggerLevel() {
        return Logger.Level.BASIC;
    }

    /**
     * 配置OkHttpClient
     *
     * @param httpClientFactory    httpClient 工厂
     * @param connectionPool       链接池配置
     * @param httpClientProperties httpClient配置
     * @return OkHttpClient
     */
    @Bean
    @ConditionalOnMissingBean(okhttp3.OkHttpClient.class)
    public okhttp3.OkHttpClient okHttp3Client(
            OkHttpClientFactory httpClientFactory,
            okhttp3.ConnectionPool connectionPool,
            FeignHttpClientProperties httpClientProperties) {
        return httpClientFactory.createBuilder(httpClientProperties.isDisableSslValidation())
                .followRedirects(httpClientProperties.isFollowRedirects())
                .writeTimeout(Duration.ofSeconds(30))
                .readTimeout(Duration.ofSeconds(30))
                .connectTimeout(Duration.ofMillis(httpClientProperties.getConnectionTimeout()))
                .connectionPool(connectionPool)
                .build();
    }

    /**
     * okhttp3 链接池配置
     *
     * @param connectionPoolFactory 链接池配置
     * @param hcp                   httpClient配置
     * @return okhttp3.ConnectionPool
     */
    @Bean
    @ConditionalOnMissingBean(okhttp3.ConnectionPool.class)
    public okhttp3.ConnectionPool okHttp3ConnectionPool(FeignHttpClientProperties hcp,
                                                        OkHttpClientConnectionPoolFactory connectionPoolFactory) {
        return connectionPoolFactory.create(hcp.getMaxConnections(), hcp.getTimeToLive(), hcp.getTimeToLiveUnit());
    }


    /**
     * 解决 RestTemplate 传递Request header
     */
    @Bean
    public RestTemplateHeaderInterceptor requestHeaderInterceptor() {
        return new RestTemplateHeaderInterceptor();
    }

    /**
     * 支持负载均衡的 LbRestTemplate, 传递请求头，一般用于内部 http 调用
     *
     * @param httpClient  OkHttpClient
     * @param interceptor RestTemplateHeaderInterceptor
     * @return LbRestTemplate
     */
    @Bean("lbRestTemplate")
    @LoadBalanced
    @SentinelRestTemplate
    @ConditionalOnMissingBean(RestTemplate.class)
    public RestTemplate lbRestTemplate(okhttp3.OkHttpClient httpClient, RestTemplateHeaderInterceptor interceptor) {
        RestTemplate lbRestTemplate = new RestTemplate(new OkHttp3ClientHttpRequestFactory(httpClient));
        lbRestTemplate.setInterceptors(Collections.singletonList(interceptor));
        this.configMessageConverters(lbRestTemplate.getMessageConverters());
        return lbRestTemplate;
    }

    /**
     * 普通的 RestTemplate，不透传请求头，一般只做外部 http 调用
     *
     * @param httpClient OkHttpClient
     * @return RestTemplate
     */
    @Bean
    @SentinelRestTemplate
    public RestTemplate restTemplate(okhttp3.OkHttpClient httpClient) {
        RestTemplate restTemplate = new RestTemplate(new OkHttp3ClientHttpRequestFactory(httpClient));
        this.configMessageConverters(restTemplate.getMessageConverters());
        return restTemplate;
    }

    private void configMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.removeIf(c -> c instanceof StringHttpMessageConverter || c instanceof MappingJackson2HttpMessageConverter);
        converters.add(new StringHttpMessageConverter(UTF_8));
        converters.add(new MappingJackson2HttpMessageConverter(this.objectMapper));
    }
}
