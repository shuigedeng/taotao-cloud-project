package com.taotao.cloud.sys.biz.controller.tools.jvm.service;


import com.taotao.cloud.sys.biz.controller.tools.jvm.service.dtos.CommandResultContext;

public interface ArthasCommandHandler {

    /**
     * 命令结果处理
     * @param commandResultContext
     */
    void process(CommandResultContext commandResultContext);
}
