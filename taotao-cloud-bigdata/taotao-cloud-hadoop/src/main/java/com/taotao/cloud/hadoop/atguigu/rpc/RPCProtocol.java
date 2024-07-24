package com.taotao.cloud.hadoop.atguigu.rpc;

public interface RPCProtocol {

    long versionID = 666;

    void mkdirs(String path);
}
