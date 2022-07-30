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

import com.taotao.cloud.web.crypto.annotation.ConditionalOnSMCrypto;
import com.taotao.cloud.web.crypto.annotation.ConditionalOnStandardCrypto;
import com.taotao.cloud.web.crypto.processor.AESCryptoProcessor;
import com.taotao.cloud.web.crypto.processor.AsymmetricCryptoProcessor;
import com.taotao.cloud.web.crypto.processor.RSACryptoProcessor;
import com.taotao.cloud.web.crypto.processor.SM2CryptoProcessor;
import com.taotao.cloud.web.crypto.processor.SM4CryptoProcessor;
import com.taotao.cloud.web.crypto.processor.SymmetricCryptoProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * <p>Description: 非对称算法配置 </p>
 *
 * @author shuigedeng
 * @version 2022.06
 * @since 2022-07-30 11:29:40
 */
@Configuration(proxyBeanMethods = false)
public class CryptoStrategyConfiguration {

    private static final Logger log = LoggerFactory.getLogger(HttpCryptoConfiguration.class);

    @PostConstruct
    public void postConstruct() {
        log.debug("SDK [Engine Asymmetric Crypto] Auto Configure.");
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnSMCrypto
    static class SMCryptoConfiguration {

        @Bean
        @ConditionalOnMissingBean
        public AsymmetricCryptoProcessor sm2CryptoProcessor() {
            SM2CryptoProcessor sm2CryptoProcessor = new SM2CryptoProcessor();
            log.debug("Strategy [SM Asymmetric SM2 Crypto Processor] Auto Configure.");
            return sm2CryptoProcessor;
        }

        @Bean
        @ConditionalOnMissingBean
        public SymmetricCryptoProcessor sm4CryptoProcessor() {
            SM4CryptoProcessor sm4CryptoProcessor = new SM4CryptoProcessor();
            log.debug("Strategy [SM Symmetric SM4 Crypto Processor] Auto Configure.");
            return sm4CryptoProcessor;
        }
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnStandardCrypto
    static class StandardCryptoConfiguration {

        @Bean
        @ConditionalOnMissingBean
        public AsymmetricCryptoProcessor rsaCryptoProcessor() {
            RSACryptoProcessor rsaCryptoProcessor = new RSACryptoProcessor();
            log.debug("Strategy [Standard Asymmetric RSA Crypto Processor] Auto Configure.");
            return rsaCryptoProcessor;
        }

        @Bean
        @ConditionalOnMissingBean
        public SymmetricCryptoProcessor aesCryptoProcessor() {
            AESCryptoProcessor aesCryptoProcessor = new AESCryptoProcessor();
            log.debug("Strategy [Standard Symmetric AES Crypto Processor] Auto Configure.");
            return aesCryptoProcessor;
        }
    }
}
