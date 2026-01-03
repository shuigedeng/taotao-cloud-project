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

package com.taotao.cloud.mq.consistency.raft.entity;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * 附加 RPC 日志返回值.
 *
 * @author shuigedeng
 */
@Setter
@Getter
@ToString
public class AentryResult implements Serializable {

    /**
     * 当前的任期号，用于领导人去更新自己
     */
    long term;

    /**
     * 跟随者包含了匹配上 prevLogIndex 和 prevLogTerm 的日志时为真
     */
    boolean success;

    public AentryResult( long term ) {
        this.term = term;
    }

    public AentryResult( boolean success ) {
        this.success = success;
    }

    public AentryResult( long term, boolean success ) {
        this.term = term;
        this.success = success;
    }

    private AentryResult( Builder builder ) {
        setTerm(builder.term);
        setSuccess(builder.success);
    }

    public static AentryResult fail() {
        return new AentryResult(false);
    }

    public static AentryResult ok() {
        return new AentryResult(true);
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    /**
     * Builder
     *
     * @author shuigedeng
     * @version 2026.02
     * @since 2025-12-19 09:30:45
     */
    public static final class Builder {

        private long term;
        private boolean success;

        private Builder() {
        }

        public Builder term( long val ) {
            term = val;
            return this;
        }

        public Builder success( boolean val ) {
            success = val;
            return this;
        }

        public AentryResult build() {
            return new AentryResult(this);
        }
    }
}
