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

package com.taotao.cloud.ccsr.api.result;

import com.google.protobuf.Any;
import com.google.protobuf.Empty;
import com.taotao.cloud.ccsr.api.grpc.auto.Response;
import com.taotao.cloud.ccsr.common.enums.ResponseCode;

/**
 * ResponseHelper
 *
 * @author shuigedeng
 * @version 2026.03
 * @since 2025-12-19 09:30:45
 */
public class ResponseHelper {

    public static Response error( int code, String msg, Any data ) {
        return Response.newBuilder()
                .setSuccess(false)
                .setData(data)
                .setCode(code)
                .setMsg(msg)
                .build();
    }

    public static Response error( int code, String msg ) {
        return error(code, msg, Any.pack(Empty.newBuilder().build()));
    }

    public static Response success( String msg, Any data ) {
        return Response.newBuilder()
                .setSuccess(true)
                .setData(data)
                .setCode(ResponseCode.SUCCESS.getCode())
                .setMsg(msg)
                .build();
    }

    public static Response success( Any data ) {
        return success(ResponseCode.SUCCESS.getMsg(), data);
    }

    public static Response success( String msg ) {
        return success(msg, Any.pack(Empty.newBuilder().build()));
    }

    public static Response success() {
        return success(ResponseCode.SUCCESS.getMsg(), Any.pack(Empty.newBuilder().build()));
    }
}
