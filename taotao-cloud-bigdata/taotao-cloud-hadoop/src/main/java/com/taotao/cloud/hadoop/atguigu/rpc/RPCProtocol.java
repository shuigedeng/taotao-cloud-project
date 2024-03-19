package com.taotao.cloud.bigdata.hadoop.atguigu.rpc;

public interface RPCProtocol {

    long versionID = 666;

    void mkdirs(String path);
}
