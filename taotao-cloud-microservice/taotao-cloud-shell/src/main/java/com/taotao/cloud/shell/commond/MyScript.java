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

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.commands.Script;

// 实现接口org.springframework.shell.standard.commands.Script.Command
@ShellComponent
public class MyScript implements Script.Command {
    // 注意：命令名称与内置命令保持一致
    @ShellMethod("Read and execute commands from a file.")
    public void script() {
        // 实现自定义逻辑
        // LogUtils.info("override default script command");
    }

    // 指定被覆盖的内置命令分组为“Built-In Commands”
    // @ShellMethod(value = "Read and execute commands from a file.", group = "Built-In Commands")
    // public void script() {
    // 	LogUtils.info("override default script command");
    // }

}
