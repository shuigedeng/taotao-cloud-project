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
import java.io.Reader;
import java.io.StringReader;
import java.util.Objects;
import java.util.stream.Stream;
import org.apache.lucene.analysis.charfilter.BaseCharFilter;
import org.apache.lucene.analysis.pattern.PatternReplaceCharFilter;

/**
 * 参考：
 *
 * @see PatternReplaceCharFilter
 */
public class EncCharFilter extends BaseCharFilter {

    private Reader transformedInput;

    public EncCharFilter(Reader in, EncNormalizer encNormalizer) {
        super(in);
    }

    @Override
    public int read(char[] cbuf, int off, int len) throws IOException {

        // 逻辑具体实现，源码看起来有点绕，还没有看明白逻辑；待后续实现 这里只暂时简单打印

        System.out.println("");
        System.out.println("--------------------------------------------------");
        // Buffer all input on the first call.
        System.out.printf("transformedInput == null:%s%n", transformedInput == null);
        if (transformedInput == null) {
            fill();
        }

        String str = new String(cbuf);
        System.out.printf("cbuf>>>:%s length:%s off>>>:%s len>>>:%s", str, str.length(), off, len);
        int read = transformedInput.read(cbuf, off, len);
        System.out.println(" read>>>" + read);
        return read;
    }

    private String fill() throws IOException {
        StringBuilder buffered = new StringBuilder();
        char[] temp = new char[1024];
        for (int cnt = input.read(temp); cnt > 0; cnt = input.read(temp)) {
            buffered.append(temp, 0, cnt);
        }
        String newStr =
                this.processPattern(buffered).toString(); // + (buffered.length() > 0 ? "" : tail);
        if (Objects.equals(newStr, "110")) {
            Stream.of(Thread.currentThread().getStackTrace()).forEach(System.out::println);
        }
        transformedInput = new StringReader(newStr);
        return newStr;
    }

    @Override
    public int read() throws IOException {
        if (transformedInput == null) {
            fill();
        }
        return transformedInput.read();
    }

    @Override
    protected int correct(int currentOff) {
        return Math.max(0, super.correct(currentOff));
    }

    /**
     * Replace pattern in input and mark correction offsets.
     */
    CharSequence processPattern(CharSequence input) {
        return input;
    }
}
