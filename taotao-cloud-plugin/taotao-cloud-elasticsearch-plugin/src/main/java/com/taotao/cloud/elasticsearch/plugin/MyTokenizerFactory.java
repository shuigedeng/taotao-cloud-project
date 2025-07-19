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

package com.taotao.cloud.elasticsearch.plugin;

import org.apache.lucene.analysis.Tokenizer;
import org.elasticsearch.env.Environment;
import org.elasticsearch.index.IndexSettings;
import org.elasticsearch.index.analysis.AbstractTokenizerFactory;

public class MyTokenizerFactory extends AbstractTokenizerFactory {

    private MyConfiguration configuration;

    /**
     * 构造函数
     * @param indexSettings 索引配置
     * @param name 分析器或者分词器名称。如果是自定义分析器，则为自定义分析器名称
     * @param env es环境配置
     * @param settings 自定义分析器配置参数
     */
    public MyTokenizerFactory(
            IndexSettings indexSettings, String name, Environment env, Settings settings) {
        super(indexSettings, settings, name);
        configuration = new MyConfiguration(indexSettings, name, env, settings);
    }

    public static TokenizerFactory getTokenizerFactory(
            IndexSettings indexSettings, Environment environment, String name, Settings settings) {
        return new MyTokenizerFactory(indexSettings, name, environment, settings).setSmart(false);
    }

    @Override
    public Tokenizer create() {
        return new MyTokenizer(configuration);
    }
}
