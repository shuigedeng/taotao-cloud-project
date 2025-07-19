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

public class TestOperator {
    public static void main(String[] args) {
        // 比较运算符
        String s1 = "hello";
        String s2 = new String("hello");

        Boolean isEqual = s1 == s2;
        System.out.println(isEqual);
        System.out.println(s1.equals(s2));

        System.out.println("========================");

        // 赋值运算符
        byte b = 10;
        b = (byte) (b + 1);
        b += 1; // 默认会做强转
        System.out.println(b);

        // 自增自减
        int x = 15;
        int y = x++;
        System.out.println("x = " + x + ", y = " + y);

        x = 15;
        y = ++x;
        System.out.println("x = " + x + ", y = " + y);

        x = 23;
        x = x++;
        System.out.println(x);
    }
}
