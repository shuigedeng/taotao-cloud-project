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

package com.taotao.cloud.realtime.datalake.mall.app.func;

import com.taotao.cloud.realtime.mall.utils.KeywordUtil;
import java.util.List;
import org.apache.flink.table.annotation.DataTypeHint;
import org.apache.flink.table.annotation.FunctionHint;
import org.apache.flink.table.functions.TableFunction;
import org.apache.flink.types.Row;

/**
 *
 * Date: 2021/2/26
 * Desc:  自定义UDTF函数实现分词操作
 */
@FunctionHint(output = @DataTypeHint("ROW<word STRING>"))
public class KeywordUDTF extends TableFunction<Row> {
    public void eval(String value) {
        // 使用工具类对字符串进行分词
        List<String> keywordList = KeywordUtil.analyze(value);
        for (String keyword : keywordList) {
            // collect(Row.of(keyword));
            Row row = new Row(1);
            row.setField(0, keyword);
            collect(row);
        }
    }
}
