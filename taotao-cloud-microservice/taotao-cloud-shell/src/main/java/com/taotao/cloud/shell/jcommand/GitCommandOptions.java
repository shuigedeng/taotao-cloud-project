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

package com.taotao.cloud.shell.jcommand;

import com.beust.jcommander.Parameter;

public class GitCommandOptions {

    @Parameter(
            names = {"help", "-help", "-h"},
            description = "查看帮助信息",
            order = 1,
            help = true)
    private boolean help;

    @Parameter(
            names = {"clone"},
            description = "克隆远程仓库数据",
            validateWith = UrlParameterValidator.class,
            order = 3,
            arity = 1)
    private String cloneUrl;

    @Parameter(
            names = {"version", "-version", "-v"},
            description = "显示当前版本号",
            order = 2)
    private boolean version = false;

    public boolean isHelp() {
        return help;
    }

    public boolean isVersion() {
        return version;
    }

    public String getCloneUrl() {
        return cloneUrl;
    }
}
