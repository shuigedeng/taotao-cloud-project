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

package com.taotao.cloud.scala; /*
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

public class TestDataTypeConversion {
    public static void main(String[] args) {
        byte b = 10;
        test(b);
        char c = 'a';
        short c2 = (short) c;
        test(c2);
    }

    public static void test(byte b) {
        System.out.println("bbbb");
    }

    public static void test(short s) {
        System.out.println("ssss");
    }

    //    public static void test(char c){
    //        System.out.println("cccc");
    //    }
    public static void test(int i) {
        System.out.println("iiii");
    }
}
