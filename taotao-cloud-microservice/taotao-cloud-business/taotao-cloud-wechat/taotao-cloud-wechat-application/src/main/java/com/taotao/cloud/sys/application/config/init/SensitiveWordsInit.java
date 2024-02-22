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

package com.taotao.cloud.sys.application.config.init;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * 敏感词汇init
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 11:54:02
 */
@Component
public class SensitiveWordsInit implements ApplicationRunner {

    // @Autowired
    // private ISensitiveWordService sensitiveWordsService;

    /** consumer 启动时，实时更新一下过滤词 */
    @Override
    public void run(ApplicationArguments args) {
        // sensitiveWordsService.resetCache();
    }
}
