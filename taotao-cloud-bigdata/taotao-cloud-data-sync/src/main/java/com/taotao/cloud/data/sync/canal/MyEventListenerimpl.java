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

package com.taotao.cloud.data.sync.canal;

import com.taotao.boot.canal.listener.DealCanalEventListener;
import com.taotao.boot.canal.option.DeleteOption;
import com.taotao.boot.canal.option.InsertOption;
import com.taotao.boot.canal.option.UpdateOption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * 实现接口方式
 *
 * @author shuigedeng
 * @version 2022.04 1.0.0
 * @since 2021/8/31 09:07
 */
@Component
public class MyEventListenerimpl extends DealCanalEventListener {

    @Autowired
    public MyEventListenerimpl(
            @Qualifier("realInsertOptoin") InsertOption insertOption,
            @Qualifier("realDeleteOption") DeleteOption deleteOption,
            @Qualifier("realUpdateOption") UpdateOption updateOption) {
        super(insertOption, deleteOption, updateOption);
    }
}
