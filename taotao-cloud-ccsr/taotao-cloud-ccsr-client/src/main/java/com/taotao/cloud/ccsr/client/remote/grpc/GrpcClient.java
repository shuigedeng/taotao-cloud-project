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

package com.taotao.cloud.ccsr.client.remote.grpc;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.protobuf.Message;
import com.taotao.cloud.ccsr.api.grpc.auto.MetadataDeleteRequest;
import com.taotao.cloud.ccsr.api.grpc.auto.MetadataReadRequest;
import com.taotao.cloud.ccsr.api.grpc.auto.MetadataWriteRequest;
import com.taotao.cloud.ccsr.api.grpc.auto.Response;
import com.taotao.cloud.ccsr.client.dto.ServerAddress;
import com.taotao.cloud.ccsr.client.future.RequestFuture;
import com.taotao.cloud.ccsr.client.remote.RpcClient;
import com.taotao.cloud.ccsr.common.exception.CcsrClientException;
import com.taotao.cloud.ccsr.common.exception.CcsrException;
import com.taotao.cloud.ccsr.spi.Join;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * GrpcClient
 *
 * @author shuigedeng
 * @version 2026.03
 * @since 2025-12-19 09:30:45
 */
@Join(order = 1, isSingleton = true)
public class GrpcClient extends GrpcConnection implements RpcClient<Message, Response> {

    /**
     * for spi load
     */
    public GrpcClient() {
        super();
    }

    public GrpcClient( String namespace, List<ServerAddress> serverAddresses ) {
        super(namespace, serverAddresses);
    }

    @Override
    public Response request( Message request ) {
        if (request instanceof MetadataWriteRequest) {
            return put((MetadataWriteRequest) request);
        } else if (request instanceof MetadataDeleteRequest) {
            return delete((MetadataDeleteRequest) request);
        } else if (request instanceof MetadataReadRequest) {
            return get((MetadataReadRequest) request);
        }
        throw new CcsrClientException(
                "GrpcClient unsupported request type: " + request.getClass().getName());
    }

    @Override
    public RequestFuture<Response> requestFuture( Message request ) {
        if (request instanceof MetadataWriteRequest) {
            return putAsync((MetadataWriteRequest) request);
        } else if (request instanceof MetadataDeleteRequest) {
            return deleteAsync((MetadataDeleteRequest) request);
        } else if (request instanceof MetadataReadRequest) {
            return getAsync((MetadataReadRequest) request);
        }
        return null;
    }

    public Response put( MetadataWriteRequest request ) {
        return blockingStub.put(request);
    }

    public Response delete( MetadataDeleteRequest request ) {
        return blockingStub.delete(request);
    }

    public Response get( MetadataReadRequest request ) {
        return blockingStub.get(request);
    }

    public RequestFuture<Response> deleteAsync( MetadataDeleteRequest request ) throws CcsrException {
        ListenableFuture<Response> future = futureStub.delete(request);
        return createRequestFuture(future);
    }

    public RequestFuture<Response> getAsync( MetadataReadRequest request ) throws CcsrException {
        ListenableFuture<Response> future = futureStub.get(request);
        return createRequestFuture(future);
    }

    public RequestFuture<Response> putAsync( MetadataWriteRequest request ) throws CcsrException {
        ListenableFuture<Response> future = futureStub.put(request);
        return createRequestFuture(future);
    }

    private RequestFuture<Response> createRequestFuture( ListenableFuture<Response> future ) {
        return new RequestFuture<>() {
            @Override
            public boolean isDone() {
                return future.isDone();
            }

            @Override
            public Response get() throws Exception {
                Response response = future.get();
                handleResponse(response);
                return response;
            }

            @Override
            public Response get( long timeout ) throws Exception {
                Response response = future.get(timeout, TimeUnit.MILLISECONDS);
                handleResponse(response);
                return response;
            }
        };
    }

    private void handleResponse( Response response ) throws CcsrException {
        if (!response.getSuccess()) {
            throw new CcsrException(response.getMsg());
        }
    }
}
