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

package com.taotao.cloud.rpc.common.util;

import com.taotao.cloud.rpc.common.enums.ResponseCode;
import com.taotao.cloud.rpc.common.exception.ReceiveResponseException;
import com.taotao.cloud.rpc.common.exception.RpcException;
import com.taotao.cloud.rpc.common.protocol.RpcRequest;
import com.taotao.cloud.rpc.common.protocol.RpcResponse;

import java.io.UnsupportedEncodingException;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * RpcMessageChecker
 *
 * @author shuigedeng
 * @version 2026.02
 * @since 2025-12-19 09:30:45
 */
@Slf4j
public class RpcMessageChecker {

    private static final String INTERFACE_NAME = "interfaceName";

    public RpcMessageChecker() {
    }

    /**
     * true 通过校验 false 校验失败
     *
     * @param rpcRequest rpc 待校验 请求包
     * @param rpcResponse rpc 待校验 响应包
     * @return boolean
     */
    public static boolean check( RpcRequest rpcRequest, RpcResponse rpcResponse )
            throws RpcException {

        if (rpcResponse == null) {
            log.error("service call failed, service: {}", rpcRequest.getInterfaceName());
            return false;
            // throw new ReceiveResponseException("service call failed Exception");
        }
        /**
         *  校验请求包 请求号 与 响应包中的 请求号 是否一致
         */
        if (!rpcResponse.getRequestId().equals(rpcRequest.getRequestId())) {
            log.error("inconsistent request numbers");
            return false;
            // throw new ReceiveResponseException("inconsistent request numbers Exception");
        }

        /**
         * 注意 rpcResponse 是 通过 反序列化 重新 实例化的 对象
         * rpcResponse.getStatusCode() 与 ResponseCode.SUCCESS.getCode() 不是同一个对象，虽然 ResponseCode 是单例模式
         * equals() 方法 判断 的 是 两个对象 的 值 相等 切忌 使用 !=
         */
        if (rpcResponse.getStatusCode() == null
                || !rpcResponse.getStatusCode().equals(ResponseCode.SUCCESS.getCode())) {
            log.error("service call failed, service: {}", rpcRequest.getInterfaceName());
            return false;
            // throw new ReceiveResponseException("service call failed Exception");
        }

        /**
         * 校验包 是否被人 修改过
         */
        try {
            String checkCode = "";
            // data 为空 校验码为 null
            if (rpcResponse.getData() == null) {
                checkCode = null;
                // data 有值 设置 校验码
            } else {
                checkCode =
                        new String(
                                DigestUtils.md5(
                                        rpcResponse.getData().toString().getBytes("UTF-8")));
            }
            // data 应为 null
            if (rpcResponse.getCheckCode() == null) {
                // 服务端校验码 与 客户端校验码 不一致
                // checkCode 由 data 计算而来，发送前 校验码为 null，此时不一致，说明 data 数据被修改
                if (checkCode != rpcResponse.getCheckCode()) {
                    log.error("data in package is modified， data: {}", rpcResponse.getData());
                    log.error(
                            "detail modification information: {}，the modification information has been filtered, and such messages will not be received and consumed！",
                            rpcResponse.getData().toString());
                    return false;
                    // throw new ReceiveResponseException("data in package is modified Exception");
                }
                // data 有值
                // 有 返回值的 情况
            } else {
                // 计算两者的 校验码，不一致则 说明 data 数据被 修改
                if (!checkCode.equals(rpcResponse.getCheckCode())) {
                    log.error("data in package is modified， data:{}", rpcResponse.getData());
                    log.error(
                            "detail modification information: {}，the modification information has been filtered, and such messages will not be received and consumed！",
                            rpcResponse.getData().toString());
                    return false;
                    // throw new ReceiveResponseException("data in package is modified Exception");
                }
            }

        } catch (UnsupportedEncodingException e) {
            log.error("package is damaged, package:{}", rpcResponse);
            return false;
            // throw new ReceiveResponseException("package is damaged Exception");
            // e.printStackTrace();
        }
        log.debug("Packet verification succeeded!");
        return true;
    }

    /**
     * 校验失败直接抛出异常，外层逻辑则会将该包抛弃不处理
     *
     * @param rpcRequest rpc 校验 请求包
     * @param rpcResponse rpc 校验 响应包
     */
    public static void checkAndThrow( RpcRequest rpcRequest, RpcResponse rpcResponse )
            throws RpcException {

        if (rpcResponse == null) {
            log.error("service call failed, service: {}", rpcRequest.getInterfaceName());
            throw new ReceiveResponseException("service call failed Exception");
        }
        /**
         *  校验请求包 请求号 与 响应包中的 请求号 是否一致
         */
        if (!rpcResponse.getRequestId().equals(rpcRequest.getRequestId())) {
            log.error("inconsistent request numbers");
            throw new ReceiveResponseException("inconsistent request numbers Exception");
        }

        /**
         * 注意 rpcResponse 是 通过 反序列化 重新 实例化的 对象
         * rpcResponse.getStatusCode() 与 ResponseCode.SUCCESS.getCode() 不是同一个对象，虽然 ResponseCode 是单例模式
         * equals() 方法 判断 的 是 两个对象 的 值 相等 切忌 使用 !=
         */
        if (rpcResponse.getStatusCode() == null
                || !rpcResponse.getStatusCode().equals(ResponseCode.SUCCESS.getCode())) {
            log.error("service call failed, service: {}", rpcRequest.getInterfaceName());
            throw new ReceiveResponseException("service call failed Exception");
        }

        /**
         * 校验包 是否被人 修改过
         */
        try {
            String checkCode = "";
            // data 为空 校验码为 null
            if (rpcResponse.getData() == null) {
                checkCode = null;
                // data 有值 设置 校验码
            } else {
                checkCode =
                        new String(
                                DigestUtils.md5(
                                        rpcResponse.getData().toString().getBytes("UTF-8")));
            }
            // data 应为 null
            if (rpcResponse.getCheckCode() == null) {
                // 服务端校验码 与 客户端校验码 不一致
                // checkCode 由 data 计算而来，发送前 校验码为 null，此时不一致，说明 data 数据被修改
                if (checkCode != rpcResponse.getCheckCode()) {
                    log.error("data in package is modified， data: {}", rpcResponse.getData());
                    log.error(
                            "detail modification information: {}，the modification information has been filtered, and such messages will not be received and consumed！",
                            rpcResponse.getData().toString());
                    throw new ReceiveResponseException("data in package is modified Exception");
                }
                // data 有值
                // 有 返回值的 情况
            } else {
                // 计算两者的 校验码，不一致则 说明 data 数据被 修改
                if (!checkCode.equals(rpcResponse.getCheckCode())) {
                    log.error("data in package is modified， data:{}", rpcResponse.getData());
                    log.error(
                            "detail modification information: {}，the modification information has been filtered, and such messages will not be received and consumed！",
                            rpcResponse.getData().toString());
                    throw new ReceiveResponseException("data in package is modified Exception");
                }
            }

        } catch (UnsupportedEncodingException e) {
            log.error("package is damaged, package:{}", rpcResponse);
            throw new ReceiveResponseException("package is damaged Exception");
            // e.printStackTrace();
        }
        log.debug("Packet verification succeeded!");
    }
}
