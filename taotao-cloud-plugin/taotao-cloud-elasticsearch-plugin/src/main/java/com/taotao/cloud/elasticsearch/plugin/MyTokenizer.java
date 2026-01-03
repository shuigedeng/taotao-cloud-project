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

import java.io.BufferedReader;
import java.io.IOException;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;

/**
 * MyTokenizer
 *
 * @author shuigedeng
 * @version 2026.02
 * @since 2025-12-19 09:30:45
 */
public final class MyTokenizer extends Tokenizer {

    // 词元文本属性
    private final CharTermAttribute termAtt;
    // 词元位移属性
    private final OffsetAttribute offsetAtt;
    // 距离
    private final PositionIncrementAttribute positionAttr;

    /**
     * 单文档当前所在的总offset，当reset（切换multi-value fields中的value）的时候不清零，在end（切换field）时清零
     */
    private int totalOffset = 0;

    private AnalyzeContext analyzeContext;

    public MyTokenizer( Configuration configuration ) {
        super();
        offsetAtt = addAttribute(OffsetAttribute.class);
        termAtt = addAttribute(CharTermAttribute.class);
        positionAttr = addAttribute(PositionIncrementAttribute.class);

        analyzeContext = new AnalyzeContext(input, configuration);
    }

    /**
     * @return 返会true告知还有下个词元，返会false告知词元输出完毕
     */
    @Override
    public boolean incrementToken() throws IOException {
        this.clearAttributes();

        int position = 0;
        Term term;
        boolean unIncreased = true;
        do {
            term = analyzeContext.next();
            if (term == null) {
                break;
            }
            if (TextUtility.isBlank(term.getText())) { // 过滤掉空白符，提高索引效率
                continue;
            }

            ++position;
            unIncreased = false;
        } while (unIncreased);

        if (term != null) {
            positionAttr.setPositionIncrement(position);
            termAtt.setEmpty().append(term.getText());
            offsetAtt.setOffset(
                    correctOffset(totalOffset + term.getOffset()),
                    correctOffset(totalOffset + term.getOffset() + term.getText().length()));
            return true;
        } else {
            totalOffset += analyzeContext.offset;
            return false;
        }
    }

    @Override
    public void end() throws IOException {
        super.end();
        offsetAtt.setOffset(totalOffset, totalOffset);
        totalOffset = 0;
    }

    /**
     * 必须重载的方法，否则在批量索引文件时将会导致文件索引失败
     */
    @Override
    public void reset() throws IOException {
        super.reset();
        analyzeContext.reset(new BufferedReader(this.input));
    }
}
