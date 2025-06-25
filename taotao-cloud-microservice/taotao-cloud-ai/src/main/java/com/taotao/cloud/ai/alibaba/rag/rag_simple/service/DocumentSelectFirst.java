package com.taotao.cloud.ai.alibaba.rag.rag_simple.service;

import org.springframework.ai.document.Document;
import org.springframework.ai.rag.Query;
import org.springframework.ai.rag.postretrieval.document.DocumentPostProcessor;

import java.util.Collections;
import java.util.List;

/**
 * @author yingzi
 * @date 2025/5/29 22:58
 */

public class DocumentSelectFirst implements DocumentPostProcessor {

    @Override
    public List<Document> process(Query query, List<Document> documents) {
        return Collections.singletonList(documents.get(0));
    }
}
