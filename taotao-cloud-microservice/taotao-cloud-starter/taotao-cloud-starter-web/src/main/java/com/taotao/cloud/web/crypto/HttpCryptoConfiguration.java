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
package com.taotao.cloud.web.crypto;

import com.taotao.cloud.web.crypto.enhance.DecryptRequestBodyAdvice;
import com.taotao.cloud.web.crypto.enhance.DecryptRequestParamMapResolver;
import com.taotao.cloud.web.crypto.enhance.DecryptRequestParamResolver;
import com.taotao.cloud.web.crypto.enhance.EncryptResponseBodyAdvice;
import com.taotao.cloud.web.crypto.processor.AsymmetricCryptoProcessor;
import com.taotao.cloud.web.crypto.processor.HttpCryptoProcessor;
import com.taotao.cloud.web.crypto.processor.SymmetricCryptoProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.annotation.PostConstruct;

/**
 * <p>Description: Rest 加密配置 </p>
 *
 * @author shuigedeng
 * @version 2022.06
 * @since 2022-07-30 11:29:35
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(CryptoProperties.class)
@Import({
        CryptoStrategyConfiguration.class,
})
public class HttpCryptoConfiguration {

    private static final Logger log = LoggerFactory.getLogger(HttpCryptoConfiguration.class);

    @PostConstruct
    public void postConstruct() {
        log.debug("SDK [Engine Rest Crypto] Auto Configure.");
    }

    @Bean
    @ConditionalOnMissingBean
    public HttpCryptoProcessor interfaceCryptoProcessor(
	    AsymmetricCryptoProcessor asymmetricCryptoProcessor, SymmetricCryptoProcessor symmetricCryptoProcessor) {
        HttpCryptoProcessor httpCryptoProcessor = new HttpCryptoProcessor(asymmetricCryptoProcessor, symmetricCryptoProcessor);
        log.trace("Bean [Interface Crypto Processor] Auto Configure.");
        return httpCryptoProcessor;
    }

    @Bean
    @ConditionalOnClass(HttpCryptoProcessor.class)
    @ConditionalOnMissingBean
    public DecryptRequestBodyAdvice decryptRequestBodyAdvice(HttpCryptoProcessor httpCryptoProcessor) {
        DecryptRequestBodyAdvice decryptRequestBodyAdvice = new DecryptRequestBodyAdvice();
        decryptRequestBodyAdvice.setInterfaceCryptoProcessor(httpCryptoProcessor);
        log.trace("Bean [Decrypt Request Body Advice] Auto Configure.");
        return decryptRequestBodyAdvice;
    }

    @Bean
    @ConditionalOnClass(HttpCryptoProcessor.class)
    @ConditionalOnMissingBean
    public EncryptResponseBodyAdvice encryptResponseBodyAdvice(HttpCryptoProcessor httpCryptoProcessor) {
        EncryptResponseBodyAdvice encryptResponseBodyAdvice = new EncryptResponseBodyAdvice();
        encryptResponseBodyAdvice.setInterfaceCryptoProcessor(httpCryptoProcessor);
        log.trace("Bean [Encrypt Response Body Advice] Auto Configure.");
        return encryptResponseBodyAdvice;
    }

    @Bean
    @ConditionalOnClass(HttpCryptoProcessor.class)
    @ConditionalOnMissingBean
    public DecryptRequestParamMapResolver decryptRequestParamStringResolver(HttpCryptoProcessor httpCryptoProcessor) {
        DecryptRequestParamMapResolver decryptRequestParamMapResolver = new DecryptRequestParamMapResolver();
        decryptRequestParamMapResolver.setInterfaceCryptoProcessor(httpCryptoProcessor);
        log.trace("Bean [Decrypt Request Param Map Resolver] Auto Configure.");
        return decryptRequestParamMapResolver;
    }

    @Bean
    @ConditionalOnClass(HttpCryptoProcessor.class)
    @ConditionalOnMissingBean
    public DecryptRequestParamResolver decryptRequestParamResolver(HttpCryptoProcessor httpCryptoProcessor) {
        DecryptRequestParamResolver decryptRequestParamResolver = new DecryptRequestParamResolver();
        decryptRequestParamResolver.setInterfaceCryptoProcessor(httpCryptoProcessor);
        log.trace("Bean [Decrypt Request Param Resolver] Auto Configure.");
        return decryptRequestParamResolver;
    }
}
