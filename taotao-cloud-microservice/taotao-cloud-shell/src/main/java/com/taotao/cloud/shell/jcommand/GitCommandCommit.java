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
import com.beust.jcommander.Parameters;

/**
 * git commit -m "desc"
 */
@Parameters(commandDescription = "提交文件", commandNames = "commit")
public class GitCommandCommit {
    public static final String COMMAND = "commit";

    @Parameter(
            names = {"-comment", "-m"},
            description = "请输入注释",
            arity = 1,
            required = true)
    private String comment;

    public String getComment() {
        return comment;
    }
}
