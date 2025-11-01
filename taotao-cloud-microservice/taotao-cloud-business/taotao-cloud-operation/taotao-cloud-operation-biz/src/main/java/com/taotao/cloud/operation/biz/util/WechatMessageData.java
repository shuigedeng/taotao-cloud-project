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

package com.taotao.cloud.operation.biz.util; // package com.taotao.cloud.message.biz.util;
//
// import lombok.Data;
import lombok.experimental.*;
//
// import java.util.HashMap;
// import java.util.LinkedHashMap;
// import java.util.List;
// import java.util.Map;
//
// /**
//  * 微信公众号消息 数据模型
//  */
// @Data
// public class WechatMessageData {
//
//     /**
//      * 抬头文字
//      */
//     private String first;
//
//     /**
//      * 备注文字
//      */
//     private String remark;
//
//     /**
//      * 消息内容
//      */
//     private List<String> messageData;
//     /**
//      * 小程序消息内容
//      */
//     private Map<String, String> mpMessageData;
//
//     /**
//      * kids
//      */
//     private List<String> kids;
//
//
//     /**
//      * 创建data数据
//      *
//      * @return
//      */
//     public String createData() {
//
//         Map<String, Map<String, String>> dataMap = new LinkedHashMap<>();
//
//         //拼接开头
//         dataMap.put("first", this.createValue(first));
//
//         //拼接关键字
//         for (int i = 0; i < messageData.size(); i++) {
//             dataMap.put("keyword" + (i + 1), createValue(this.messageData.get(i)));
//         }
//         //拼接备注
//         dataMap.put("remark", createValue(this.remark));
//
//         return JSONUtil.toJsonStr(dataMap);
//     }
//
//
//     /**
//      * 创建data数据
//      *
//      * @return
//      */
//     public Map<String, Map<String, String>> createMPData() {
//
//         LinkedHashMap<String, Map<String, String>> dataMap = new LinkedHashMap<>();
//
//         for (String key : mpMessageData.keySet()) {
//             dataMap.put(key, createValue(mpMessageData.get(key)));
//         }
//         return dataMap;
//     }
//
//     /**
//      * 创建统一格式的map
//      *
//      * @param msg
//      * @return
//      */
//     private Map<String, String> createValue(String msg) {
//         Map<String, String> map = new HashMap<>(2);
//         map.put("value", msg);
//         return map;
//     }
//
//
// }
