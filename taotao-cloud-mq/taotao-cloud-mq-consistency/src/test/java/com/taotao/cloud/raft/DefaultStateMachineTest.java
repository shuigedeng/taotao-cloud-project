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
import com.taotao.cloud.raft.impl.DefaultStateMachine;
import org.junit.jupiter.api.Test;
import org.rocksdb.RocksDBException;

/**
 *
 * @author shuigedeng
 */
public class DefaultStateMachineTest {
    static DefaultStateMachine machine = DefaultStateMachine.getInstance();

    static {
        System.setProperty("serverPort", "8777");
        machine.dbDir =
                "/Users/cxs/code/lu-raft-revert/rocksDB-raft/" + System.getProperty("serverPort");
        machine.stateMachineDir = machine.dbDir + "/stateMachine";
    }

    @Before
    public void before() {
        machine = DefaultStateMachine.getInstance();
    }

    @Test
    public void apply() {
        LogEntry logEntry =
                LogEntry.builder()
                        .term(1)
                        .command(Command.builder().key("hello").value("value1").build())
                        .build();
        machine.apply(logEntry);
    }

    @Test
    public void applyRead() throws RocksDBException {

        System.out.println(machine.get("hello:7"));
    }
}
