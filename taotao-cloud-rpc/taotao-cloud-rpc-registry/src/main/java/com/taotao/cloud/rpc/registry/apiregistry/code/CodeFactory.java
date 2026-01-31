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

package com.taotao.cloud.rpc.registry.apiregistry.code;

import com.taotao.cloud.rpc.registry.apiregistry.base.ApiRegistryException;

import java.util.HashMap;
import java.util.Map;

/**
 * CodeFactory
 *
 * @author shuigedeng
 * @version 2026.03
 * @since 2025-12-19 09:30:45
 */
public class CodeFactory {

    public static Code create( String contentType ) {
        //        contentType = StringUtils.nullToEmpty(contentType).toLowerCase();
        if (contentType.contains("json")) {
            return new JsonCode();
        } else if (contentType.contains("protostuff") || contentType.contains("protobuf")) {
            return new ProtostuffCode();
        }
        return create();
    }

    public static Code create() {
        //		String codeType = ApiRegistryProperties.getRegistryCodeType();
        //        if("json".equalsIgnoreCase(codeType)){
        //            return new JsonCode();
        //        }else
        // if("protostuff".equalsIgnoreCase(codeType)||"protobuf".equalsIgnoreCase(codeType)){
        //            return new ProtostuffCode();
        //        }
        throw new ApiRegistryException("请配置ttc.apiRegistry.code.type");
    }

    public static String getHeader() {
        //		String codeType = ApiRegistryProperties.getRegistryCodeType();
        //		String header = codeHeadersEnum.get(codeType);
        //        if(header==null){
        //            throw new ApiRegistryException("请配置ttc.apiRegistry.code.type");
        //        }
        return null;
    }

    private static Map<String, String> codeHeadersEnum =
            new HashMap<String, String>() {
                {
                    put("json", "application/json");
                    put("protobuf", "application/x-protobuf");
                    put("protostuff", "application/x-protobuf");
                }
            };
}
