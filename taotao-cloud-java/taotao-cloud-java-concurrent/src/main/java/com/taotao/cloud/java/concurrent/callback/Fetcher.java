package com.taotao.cloud.java.concurrent.callback;

public interface Fetcher {
    void fetchData(FetcherCallback callback);
}
