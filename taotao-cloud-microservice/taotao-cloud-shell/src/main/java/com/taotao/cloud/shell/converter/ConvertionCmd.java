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

package com.taotao.cloud.shell.converter;

import org.springframework.stereotype.Component;
import org.springframework.shell.core.command.annotation.Command;

// 使用自定义转换类型
@Component
public class ConvertionCmd {
    // #food apple
    // Food{value='apple'}
    // 在命令方法中直接可以获取Food对象，这是通过前面的自定义类型转换器MyConverter实现的
    @Command(name = "changePassword", description = "Conversion food")
    public String food(Food food) {
        return food.toString();
    }
}
