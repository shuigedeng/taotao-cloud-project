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

package com.taotao.cloud.mq.common.util;

import com.taotao.boot.common.utils.common.ArgUtils;
import com.taotao.cloud.mq.common.rpc.RpcAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * 内部地址工具类
 *
 * @author shuigedeng
 * @since 2024.05
 */
public final class InnerAddressUtils {

    private InnerAddressUtils() {}

    /**
     * 初始化地址信息
     *
     * @param address 地址
     * @return 结果列表
     * @since 2024.05
     */
    public static List<RpcAddress> initAddressList(String address) {
        ArgUtils.notEmpty(address, "address");

        String[] strings = address.split(",");
        List<RpcAddress> list = new ArrayList<>();
        for (String s : strings) {
            String[] infos = s.split(":");

            RpcAddress rpcAddress = new RpcAddress();
            rpcAddress.setAddress(infos[0]);
            rpcAddress.setPort(Integer.parseInt(infos[1]));
            if (infos.length > 2) {
                rpcAddress.setWeight(Integer.parseInt(infos[2]));
            } else {
                rpcAddress.setWeight(1);
            }
            list.add(rpcAddress);
        }
        return list;
    }
}
