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

import static com.taotao.cloud.common.utils.lang.StringUtils.format;

import com.taotao.cloud.common.utils.log.LogUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ItemReadListener;

/**
 * @Author : JCccc
 * @CreateTime : 2020/3/17
 * @Description :
 **/
public class MybatisReadListener implements ItemReadListener<BlogInfo> {

    private Logger logger = LoggerFactory.getLogger(MybatisReadListener.class);

    @Override
    public void beforeRead() {}

    @Override
    public void afterRead(BlogInfo item) {}

    @Override
    public void onReadError(Exception ex) {
        try {
            logger.info(format("%s%n", ex.getMessage()));
        } catch (Exception e) {
            LogUtils.error(e);
        }
    }
}
