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

/**
 *
 * 请求投票 RPC 返回值.
 *
 */
@Getter
@Setter
public class RvoteResult implements Serializable {

    /**
     * 当前任期号，以便于候选人去更新自己的任期
     */
    long term;

    /**
     * 候选人赢得了此张选票时为真
     */
    boolean voteGranted;

    public RvoteResult( boolean voteGranted ) {
        this.voteGranted = voteGranted;
    }

    private RvoteResult( Builder builder ) {
        setTerm(builder.term);
        setVoteGranted(builder.voteGranted);
    }

    public static RvoteResult fail() {
        return new RvoteResult(false);
    }

    public static RvoteResult ok() {
        return new RvoteResult(true);
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
        private boolean voteGranted;

        private Builder() {
        }

        public Builder term( long term ) {
            this.term = term;
            return this;
        }

        public Builder voteGranted( boolean voteGranted ) {
            this.voteGranted = voteGranted;
            return this;
        }

        public RvoteResult build() {
            return new RvoteResult(this);
        }
    }
}
