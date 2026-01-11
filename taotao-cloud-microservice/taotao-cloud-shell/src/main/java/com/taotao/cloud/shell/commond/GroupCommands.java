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
 * 为命令分组
 */
@Component
public class GroupCommands {

    @Command(name = "download1", description = "命令1")
    public void download1() {}

    @Command(name = "download2", description = "命令2")
    public void download2() {}

    @Command(name = "download3", description = "命令3")
    public void download3() {}

    @Command(name = "download4", description = "命令4", group = "其他组")
    public void download4() {}
}
