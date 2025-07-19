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

package com.taotao.cloud.elasticsearch.plugin.hello;

import java.io.IOException;
import java.util.Date;
import org.elasticsearch.client.internal.node.NodeClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.rest.BaseRestHandler;
import org.elasticsearch.rest.RestController;
import org.elasticsearch.rest.RestRequest;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.xcontent.XContentBuilder;

/**
 * @author caspar
 * @date 2018/9/16
 **/
public class HelloHandler extends BaseRestHandler {

    protected HelloHandler(Settings settings, RestController restController) {
        super(settings);
        // api的url映射
        restController.registerHandler(RestRequest.Method.GET, "/_hello", this);
    }

    @Override
    protected RestChannelConsumer prepareRequest(RestRequest restRequest, NodeClient nodeClient)
            throws IOException {
        // 接收的参数
        System.out.println("params==" + restRequest.params());

        long t1 = System.currentTimeMillis();

        String name = restRequest.param("name");

        long cost = System.currentTimeMillis() - t1;
        // 返回内容，这里返回一个处理业务逻辑的发费时间，前端传的name，以及当前时间。
        return channel -> {
            XContentBuilder builder = channel.newBuilder();
            builder.startObject();
            builder.field("cost", cost);
            builder.field("name", name);
            builder.field("time", new Date());
            builder.endObject();
            channel.sendResponse(new BytesRestResponse(RestStatus.OK, builder));
        };
    }
}
