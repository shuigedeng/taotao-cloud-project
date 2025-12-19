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

package com.taotao.cloud.ccsr.core.remote.raft;

import com.alipay.sofa.jraft.Closure;
import com.alipay.sofa.jraft.Status;
import com.alipay.sofa.jraft.error.RaftError;
import com.google.protobuf.Message;
import com.taotao.cloud.ccsr.api.grpc.auto.Response;
import com.taotao.cloud.ccsr.common.log.Log;

/**
 * @author shuigedeng
 */
public class RaftClosure implements Closure {

    private Message message;

    private Closure closure;

    private RaftStatus _status = new RaftStatus();

    public RaftClosure( Message message ) {
        this.message = message;
    }

    public RaftClosure( Message message, Closure closure ) {
        this.message = message;
        this.closure = closure;
    }

    public void setResponse( Response response ) {
        this._status.setResponse(response);
    }

    public void setThrowable( Throwable throwable ) {
        this._status.setThrowable(throwable);
    }

    public Message getMessage() {
        return message;
    }

    private void clear() {
        message = null;
        closure = null;
        _status = null;
    }

    @Override
    public void run( Status status ) {
        if (status == null) {
            Log.print("RaftClosure#run方法的【status】是空值");
        }
        _status.setStatus(status);
        closure.run(_status);
        clear();
    }

    /**
     * RaftStatus
     *
     * @author shuigedeng
     * @version 2026.01
     * @since 2025-12-19 09:30:45
     */
    public static class RaftStatus extends Status {

        private Status status;

        private Response response = null;

        private Throwable throwable = null;

        public void setStatus( Status status ) {
            this.status = status;
        }

        @Override
        public void reset() {
            status.reset();
        }

        @Override
        public boolean isOk() {
            return status.isOk();
        }

        @Override
        public int getCode() {
            return status.getCode();
        }

        @Override
        public void setCode( int code ) {
            status.setCode(code);
        }

        @Override
        public RaftError getRaftError() {
            return status.getRaftError();
        }

        @Override
        public void setError( int code, String fmt, Object... args ) {
            status.setError(code, fmt, args);
        }

        @Override
        public void setError( RaftError error, String fmt, Object... args ) {
            status.setError(error, fmt, args);
        }

        @Override
        public String toString() {
            return status.toString();
        }

        @Override
        public Status copy() {
            RaftStatus copy = new RaftStatus();
            copy.status = this.status;
            copy.response = this.response;
            copy.throwable = this.throwable;
            return copy;
        }

        @Override
        public String getErrorMsg() {
            return status.getErrorMsg();
        }

        @Override
        public void setErrorMsg( String errMsg ) {
            status.setErrorMsg(errMsg);
        }

        public Response getResponse() {
            return response;
        }

        public void setResponse( Response response ) {
            this.response = response;
        }

        public Throwable getThrowable() {
            return throwable;
        }

        public void setThrowable( Throwable throwable ) {
            this.throwable = throwable;
        }
    }
}
