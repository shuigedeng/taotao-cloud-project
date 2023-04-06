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

package com.taotao.cloud.auth.biz.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * IdGenerator
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2022-03-28 11:29:30
 */
public class IdGenerator {

    public static String generateAuthorizationId(String id) {
        String currentTime = DateTimeFormatter.ofPattern("yyyyMMddhhmmssSSS").format(LocalDateTime.now());
        return id + currentTime;
    }

    // public static void main(String[] args) {
    //	System.out.println(new BCryptPasswordEncoder().encode("123456"));
    // }

    // public static void main(String[] args) {
    //	System.out.println(new BCryptPasswordEncoder().encode("123456"));
    // }

}
