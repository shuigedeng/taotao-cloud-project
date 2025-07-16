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

package com.taotao.cloud.ccsr.client.client.filter;

import com.taotao.cloud.ccsr.client.context.CcsrContext;
import com.taotao.cloud.ccsr.client.lifecycle.LeftCycle;
import com.taotao.cloud.ccsr.client.request.Payload;

public interface Filter<S, OPTION> extends LeftCycle {

    Filter<S, OPTION> next(Filter<S, OPTION> filter);

    Filter<S, OPTION> next();

    Filter<S, OPTION> pre(Filter<S, OPTION> filter);

    Filter<S, OPTION> pre();

    S preFilter(CcsrContext context, OPTION option, Payload request);

    S postFilter(CcsrContext context, OPTION option, Payload request, S response);
}
