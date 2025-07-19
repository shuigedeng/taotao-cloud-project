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

import java.io.IOException;
import java.io.Reader;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class AnalyzeContext {

    /**
     * 输入
     */
    private Reader input;

    /**
     * 配置
     */
    private Configuration configuration;

    /**
     * 分词结果
     */
    private Iterator<Term> iterator;

    /**
     * term的偏移量，由于wrapper是按行读取的，必须对term.offset做一个校正
     */
    int offset;

    /**
     * 缓冲区大小
     */
    private static final int BUFFER_SIZE = 4096;

    /**
     * 缓冲区
     */
    private char[] buffer = new char[BUFFER_SIZE];

    /**
     * 缓冲区未处理的下标
     */
    private int remainSize = 0;

    /**
     * 句子分隔符
     */
    private static final Set<Character> delimiterCharSet =
            new HashSet<Character>() {
                {
                    add('\r');
                    add('\n');
                    add('。');
                    add('!');
                    add('！');
                    add('，');
                    add(',');
                    add('?');
                    add('？');
                    add(';');
                    add('；');
                }
            };

    public AnalyzeContext(Reader reader, Configuration configuration) {
        this.input = reader;
        this.configuration = configuration;
    }

    /**
     * 重置分词器
     *
     * @param reader
     */
    public void reset(Reader reader) {
        input = reader;
        offset = 0;
        iterator = null;
    }

    public Term next() throws IOException {
        // 如果当年迭代器中还有词，继续迭代
        if (iterator != null && iterator.hasNext()) {
            return iterator.next();
        }
        // 没词，读取下一行
        String line = readLine();

        if (line == null) {
            return null;
        }

        // todo
        // List<Term> termList = [你的分词算法].getTextTokenizer(line, configuration);
        //// 分词结果是空
        // if (termList.size() == 0) {
        //    return null;
        // }

        // for (Term term : termList) {
        //    term.setOffset(term.getOffset() + offset);
        // }
        // offset += line.length();
        // iterator = termList.iterator();
        return iterator.next();
    }

    private String readLine() throws IOException {
        int offset = 0;
        int length = BUFFER_SIZE;
        // 上次读取剩下的部分
        if (remainSize > 0) {
            offset = remainSize;
            length -= remainSize;
        }
        // 读取的字符数，-1 读取结束
        int n = input.read(buffer, offset, length);
        if (n < 0) {
            if (remainSize != 0) {
                String lastLine = new String(buffer, 0, remainSize);
                remainSize = 0;
                return lastLine;
            }
            return null;
        }
        n += offset;

        // 真正的句子结束位置
        int eos = lastIndexOfEos(buffer, n);
        String line = new String(buffer, 0, eos);
        remainSize = n - eos;
        if (remainSize > 0) {
            // 把剩下的复制到缓冲区开始位置
            System.arraycopy(buffer, eos, buffer, 0, remainSize);
        }
        return line;
    }

    /**
     * 根据句子分隔符，找到这一段文本中的最后一句话所在位置。
     *
     * @param buffer
     * @param length
     * @return
     */
    private int lastIndexOfEos(char[] buffer, int length) {
        if (length < BUFFER_SIZE) {
            return length;
        }
        for (int i = length - 1; i > 0; i--) {
            if (delimiterCharSet.contains(buffer[i])) {
                return i + 1;
            }
        }
        return length;
    }
}
