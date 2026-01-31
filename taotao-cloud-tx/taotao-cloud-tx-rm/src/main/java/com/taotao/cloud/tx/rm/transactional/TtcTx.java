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

package com.taotao.cloud.tx.rm.transactional;

import com.taotao.cloud.tx.rm.util.Task;

// 分布式事务 - 子事务对象
/**
 * TtcTx
 *
 * @author shuigedeng
 * @version 2026.03
 * @since 2025-12-19 09:30:45
 */
public class TtcTx {

    // 当前子事务属于哪个事务组
    private String groupId;
    // 当前子事务的事务ID
    private String transactionalId;
    // 当前子事务的事务类型
    private TransactionalType transactionalType;
    // 当前子事务的任务等待队列（基于此实现事务控制权）
    private Task task;

    public TtcTx( String groupId, String transactionalId, TransactionalType transactionalType ) {
        this.groupId = groupId;
        this.transactionalId = transactionalId;
        this.transactionalType = transactionalType;
        this.task = new Task();
    }

    public TtcTx( String groupId, String transactionalId ) {
        this.groupId = groupId;
        this.transactionalId = transactionalId;
        this.task = new Task();
    }

    public TtcTx() {
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId( String groupId ) {
        this.groupId = groupId;
    }

    public String getTransactionalId() {
        return transactionalId;
    }

    public void setTransactionalId( String transactionalId ) {
        this.transactionalId = transactionalId;
    }

    public TransactionalType getTransactionalType() {
        return transactionalType;
    }

    public void setTransactionalType( TransactionalType transactionalType ) {
        this.transactionalType = transactionalType;
    }

    public Task getTask() {
        return task;
    }

    public void setTask( Task task ) {
        this.task = task;
    }
}
