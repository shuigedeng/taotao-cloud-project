package com.taotao.cloud.sys.biz.tools.jvm.service.handlers;


import com.taotao.cloud.sys.biz.tools.jvm.service.ArthasCommandHandler;
import com.taotao.cloud.sys.biz.tools.jvm.service.dtos.CommandResultContext;

public class NoHandleCommandHandler  implements ArthasCommandHandler {
    @Override
    public void process(CommandResultContext commandResultContext) {
        commandResultContext.setResult(commandResultContext.getOrigin());
    }
}
