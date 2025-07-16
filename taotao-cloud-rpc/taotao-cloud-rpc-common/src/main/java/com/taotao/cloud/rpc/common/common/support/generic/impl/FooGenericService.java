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

package com.taotao.cloud.rpc.common.common.support.generic.impl;

import com.taotao.cloud.rpc.common.common.exception.GenericException;
import com.taotao.cloud.rpc.common.common.support.generic.GenericService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 最简单的泛化调用实现
 *
 * @author shuigedeng
 * @since 0.1.2
 */
public final class FooGenericService implements GenericService {

    private static final Logger LOG = LoggerFactory.getLogger(FooGenericService.class);

    @Override
    public Object $invoke(String method, String[] parameterTypes, Object[] args)
            throws GenericException {
        //        LOG.info("[Generic] method: {}", method);
        //        LOG.info("[Generic] parameterTypes: {}", Arrays.toString(parameterTypes));
        //        LOG.info("[Generic] args: {}", args);
        return null;
    }
}
