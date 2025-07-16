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

package com.taotao.cloud.rpc.common.common.config.component;

import java.util.List;

/**
 * 地址信息工具类
 * @author shuigedeng
 * @since 2024.06
 */
public final class RpcAddressBuilder {

    private RpcAddressBuilder() {}

    /**
     * 将地址信息转换对对象信息
     * @param addresses 地址信息
     * @return 转换后的信息列表
     * @since 2024.06
     */
    public static List<RpcAddress> of(final String addresses) {
        //        ArgUtil.notEmpty(addresses, "addresses");

        //        String[] addressArray = addresses.split(PunctuationConst.COMMA);
        //        ArgUtil.notEmpty(addressArray, "addresses");

        //        List<RpcAddress> rpcAddressList = Guavas.newArrayList(addressArray.length);
        //        for(String address : addressArray) {
        //            String[] addressSplits = address.split(PunctuationConst.COLON);
        //            if (addressSplits.length < 2) {
        //                throw new IllegalArgumentException("Address must be has ip and port, like
        // 127.0.0.1:9527");
        //            }
        //            String ip = addressSplits[0];
        //            int port = NumUtil.toIntegerThrows(addressSplits[1]);
        //            // 包含权重信息
        //            int weight = 1;
        //            if (addressSplits.length >= 3) {
        //                weight = NumUtil.toInteger(addressSplits[2], 1);
        //            }
        //
        //            RpcAddress rpcAddress = new RpcAddress(ip, port, weight);
        //            rpcAddressList.add(rpcAddress);
        //        }
        //        return rpcAddressList;
        return null;
    }
}
