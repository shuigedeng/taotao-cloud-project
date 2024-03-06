/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.rocketmq.flink.legacy.common.selector;

import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;

import org.junit.Assert;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class HashMessageQueueSelectorTest {

    @Test
    public void testSelect() {
        MessageQueueSelector hash = new HashMessageQueueSelector();
        List<MessageQueue> queues = new ArrayList<>();
        MessageQueue queue0 = new MessageQueue("test", "broker-a", 0);
        MessageQueue queue1 = new MessageQueue("test", "broker-b", 1);
        MessageQueue queue2 = new MessageQueue("test", "broker-c", 2);
        queues.add(queue0);
        queues.add(queue1);
        queues.add(queue2);

        Message message = new Message("test", "*", "1", "body".getBytes(StandardCharsets.UTF_8));

        MessageQueue messageQueue = hash.select(queues, message, 1);
        Assert.assertEquals(messageQueue.getQueueId(), 1);
    }
}
