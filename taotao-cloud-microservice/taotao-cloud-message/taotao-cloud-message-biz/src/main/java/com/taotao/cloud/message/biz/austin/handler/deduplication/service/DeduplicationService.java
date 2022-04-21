package com.taotao.cloud.message.biz.austin.handler.deduplication.service;


import com.taotao.cloud.message.biz.austin.handler.deduplication.DeduplicationParam;

public interface DeduplicationService {

    /**
     * 去重
     * @param param
     */
    void deduplication(DeduplicationParam param);
}
