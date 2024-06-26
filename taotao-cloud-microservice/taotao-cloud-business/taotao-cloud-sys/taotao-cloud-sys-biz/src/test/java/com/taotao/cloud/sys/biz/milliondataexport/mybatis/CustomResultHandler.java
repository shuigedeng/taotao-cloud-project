package com.taotao.cloud.sys.biz.milliondataexport.mybatis;

import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;

public class CustomResultHandler implements ResultHandler {

    private final DownloadProcessor downloadProcessor;

    public CustomResultHandler(
            DownloadProcessor downloadProcessor) {
        super();
        this.downloadProcessor = downloadProcessor;
    }

    @Override
    public void handleResult(ResultContext resultContext) {
        Authors authors = (Authors) resultContext.getResultObject();
        downloadProcessor.processData(authors);
    }
}
