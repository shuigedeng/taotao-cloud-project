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

package com.taotao.cloud.xxljob.core.util;

import freemarker.ext.beans.BeansWrapper;
import freemarker.ext.beans.BeansWrapperBuilder;
import freemarker.template.Configuration;
import freemarker.template.TemplateHashModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ftl util
 *
 * @author xuxueli 2018-01-17 20:37:48
 */
public class FtlUtil {
    private static Logger logger = LoggerFactory.getLogger(FtlUtil.class);

    private static BeansWrapper wrapper =
            new BeansWrapperBuilder(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS)
                    .build(); // BeansWrapper.getDefaultInstance();

    public static TemplateHashModel generateStaticModel(String packageName) {
        try {
            TemplateHashModel staticModels = wrapper.getStaticModels();
            TemplateHashModel fileStatics = (TemplateHashModel) staticModels.get(packageName);
            return fileStatics;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }
}
