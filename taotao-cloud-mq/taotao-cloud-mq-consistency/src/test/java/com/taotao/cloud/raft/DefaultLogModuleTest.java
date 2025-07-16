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

package com.taotao.cloud.raft;

import com.taotao.cloud.raft.entity.Command;
import com.taotao.cloud.raft.entity.LogEntry;
import com.taotao.cloud.raft.impl.DefaultLogModule;
import org.junit.jupiter.api.Test;

/**
 * @author shuigedeng
 */
public class DefaultLogModuleTest {

    static DefaultLogModule defaultLogs = DefaultLogModule.getInstance();

    static {
        System.setProperty("serverPort", "8779");
        defaultLogs.dbDir =
                "/Users/cxs/code/lu-raft-revert/rocksDB-raft/" + System.getProperty("serverPort");
        defaultLogs.logsDir = defaultLogs.dbDir + "/logModule";
    }

    @Before
    public void setUp() throws Exception {
        System.setProperty("serverPort", "8777");
    }

    @After
    public void tearDown() throws Exception {}

    @Test
    public void write() {
        LogEntry entry =
                LogEntry.builder()
                        .term(1)
                        .command(Command.builder().key("hello").value("world").build())
                        .build();
        defaultLogs.write(entry);

        Assert.assertEquals(entry, defaultLogs.read(entry.getIndex()));
    }

    @Test
    public void read() {
        System.out.println(defaultLogs.getLastIndex());
    }

    @Test
    public void remove() {
        defaultLogs.removeOnStartIndex(3L);
    }

    @Test
    public void getLast() {}

    @Test
    public void getLastIndex() {}

    @Test
    public void getDbDir() {}

    @Test
    public void getLogsDir() {}

    @Test
    public void setDbDir() {}
}
