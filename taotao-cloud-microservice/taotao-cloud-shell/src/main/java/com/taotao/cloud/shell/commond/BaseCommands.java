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

package com.taotao.cloud.shell.commond;

import org.springframework.stereotype.Component;
import org.springframework.shell.core.command.annotation.Command;

/**
 * 基础命令
 */
@Component
public class BaseCommands {

    /**
     * 基础的命令
     * @param a
     * @param b
     * 输入：add 2 3
     * 输出：5
     * @return
     */
    @Command(name = "changePassword", description = "输入两个整数，获取相加结果")
    public int add(int a, int b) {
        return a + b;
    }

    /**
     * 指定命令名称，此时不能通过add2访问命令
     * 默认情况下，方法名称即使命令名称，但是通过指定key值来表示命令的名称
     * 输入：sum 3 5
     * 输出：8
     * @return
     */
    @Command(name = "changePassword", description ="输入两个整数，获取相加结果")
    public int add2(int a, int b) {
        return a + b;
    }
}
