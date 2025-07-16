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

package com.taotao.cloud.mq.consistency.raft1.common.entity.req;

/**
 * 客户端请求
 *
 */
public class ClientKeyValueRequest extends BaseRaftRequest {

    public static int PUT = 0;
    public static int GET = 1;

    private int type;

    private String key;

    private String value;

    public static int getPUT() {
        return PUT;
    }

    public static void setPUT(int PUT) {
        ClientKeyValueRequest.PUT = PUT;
    }

    public static int getGET() {
        return GET;
    }

    public static void setGET(int GET) {
        ClientKeyValueRequest.GET = GET;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public enum Type {
        /** 1111 */
        PUT(0),
        GET(1);
        int code;

        Type(int code) {
            this.code = code;
        }

        public static Type value(int code) {
            for (Type type : values()) {
                if (type.code == code) {
                    return type;
                }
            }
            return null;
        }
    }

    @Override
    public String toString() {
        return "ClientKeyValueRequest{"
                + "type="
                + type
                + ", key='"
                + key
                + '\''
                + ", value='"
                + value
                + '\''
                + '}';
    }
}
