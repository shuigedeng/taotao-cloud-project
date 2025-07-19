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

import java.io.IOException;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.elasticsearch.env.Environment;
import org.elasticsearch.index.IndexSettings;
import org.elasticsearch.index.analysis.AbstractTokenizerFactory;

public class LowercaseTokenizerFactory extends AbstractTokenizerFactory {

    public LowercaseTokenizerFactory(
            IndexSettings indexSettings, Environment environment, String name, Settings settings) {
        super(indexSettings, settings, name);
    }

    @Override
    public Tokenizer create() {
        return new LowercaseTokenizer();
    }

    private static class LowercaseTokenizer extends Tokenizer {

        private CharTermAttribute termAttr;

        LowercaseTokenizer() {
            termAttr = addAttribute(CharTermAttribute.class);
        }

        @Override
        public boolean incrementToken() throws IOException {
            clearAttributes();
            if (input.incrementToken()) {
                String term = termAttr.toString().toLowerCase();
                termAttr.setEmpty().append(term);
                return true;
            } else {
                return false;
            }
        }
    }
}
