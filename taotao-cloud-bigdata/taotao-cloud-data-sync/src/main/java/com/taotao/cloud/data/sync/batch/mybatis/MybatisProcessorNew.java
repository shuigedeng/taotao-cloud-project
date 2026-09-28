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

package com.taotao.cloud.data.sync.batch.mybatis;

import org.springframework.batch.item.validator.ValidatingItemProcessor;
import org.springframework.batch.item.validator.ValidationException;

/**
 * @Author : JCccc
 * @CreateTime : 2020/3/17
 * @Description :
 **/
public class MybatisProcessorNew extends ValidatingItemProcessor<BlogInfo> {

    @Override
    public BlogInfo process(BlogInfo item) throws ValidationException {
        /**
         * 需要执行super.process(item)才会调用自定义校验器
         */
        super.process(item);
        /**
         * 对数据进行简单的处理
         */
        int authorId = Integer.parseInt(item.getBlogAuthor());
        if (authorId < 20000) {
            item.setBlogTitle("这是都是小于20000的数据");
        } else if (authorId > 20000 && authorId < 30000) {
            item.setBlogTitle("这是都是小于30000但是大于20000的数据");
        } else {
            item.setBlogTitle("旧书不厌百回读");
        }
        return item;
    }
}
