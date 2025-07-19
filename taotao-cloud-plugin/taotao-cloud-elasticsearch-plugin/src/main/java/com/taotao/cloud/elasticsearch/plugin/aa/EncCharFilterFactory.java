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

package com.taotao.cloud.elasticsearch.plugin.aa;

import java.io.Reader;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;
import org.elasticsearch.index.IndexSettings;
import org.elasticsearch.index.analysis.AbstractCharFilterFactory;
import org.elasticsearch.index.analysis.NormalizingCharFilterFactory;

/**
 * 参考：
 * org.elasticsearch.analysis.common.PatternReplaceCharFilterFactory
 * org.elasticsearch.analysis.common.MappingCharFilterFactory
 */
public class EncCharFilterFactory extends AbstractCharFilterFactory
        implements NormalizingCharFilterFactory {

    private EncNormalizer normalizer = null;

    public EncCharFilterFactory(IndexSettings indexSettings, String name) {
        super(indexSettings, name);
        normalizer = new EncNormalizerImpl();
    }

    public EncCharFilterFactory(
            IndexSettings indexSettings, Environment environment, String name, Settings settings) {
        super(indexSettings, name);
        normalizer = new EncNormalizerImpl();
    }

    @Override
    public Reader create(Reader reader) {
        return new com.caspar.es.plugin.hello.EncCharFilter(reader, normalizer);
    }
}
