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

package org.apache.rocketmq.flink.legacy.common.util;

import org.apache.rocketmq.flink.legacy.RunningChecker;

import junit.framework.TestCase;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/** Tests for {@link RetryUtil}. */
@Slf4j
public class RetryUtilTest extends TestCase {

    public void testCall() {
        try {
            User user = new User();
            RunningChecker runningChecker = new RunningChecker();
            runningChecker.setState(RunningChecker.State.RUNNING);
            ExecutorService executorService = Executors.newCachedThreadPool();
            executorService.execute(
                    () ->
                            RetryUtil.call(
                                    () -> {
                                        user.setName("test");
                                        user.setAge(Integer.parseInt("12e"));
                                        return true;
                                    },
                                    "Something is error",
                                    runningChecker));
            Thread.sleep(10000);
            executorService.shutdown();
            log.info("Thread has finished");
            assertEquals(0, user.age);
            assertEquals("test", user.name);
            assertEquals(false, runningChecker.isRunning());
        } catch (Exception e) {
            log.warn("Exception has been caught");
        }
    }

    @Data
    public class User {
        String name;
        int age;
    }
}
