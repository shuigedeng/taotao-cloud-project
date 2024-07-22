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

package com.taotao.cloud.sys.api.dubbo;


import com.taotao.cloud.sys.api.dubbo.response.FileRpcResponse;

/**
 * ISysDictService
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-10-09 20:32:36
 */
public interface FileRpcService {

    /**
     * 字典code查询
     *
     * @param code 代码
     * @return {@link FileRpcResponse }
     * @since 2022-06-29 21:45:44
     */
    FileRpcResponse findByCode(Integer code);
}
