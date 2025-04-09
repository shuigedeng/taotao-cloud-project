package com.taotao.cloud.ccsr.client.remote;


import com.taotao.cloud.ccsr.api.grpc.auto.MetadataDeleteRequest;
import com.taotao.cloud.ccsr.api.grpc.auto.MetadataReadRequest;
import com.taotao.cloud.ccsr.api.grpc.auto.MetadataWriteRequest;
import com.taotao.cloud.ccsr.api.grpc.auto.Response;
import com.taotao.cloud.ccsr.spi.SPI;
import org.ohara.msc.future.RequestFuture;
import org.ohara.msc.lifecycle.Closeable;

@SPI(value = "grpc")
public interface RpcClient<R,S> {

    S request(R request);

    RequestFuture<S> requestFuture(R request);


}
