package com.taotao.cloud.sys.biz.config.arthas;


import com.taotao.cloud.sys.api.dto.jvm.CommandResultContext;

public class NoHandleCommandHandler implements ArthasCommandHandler {

	@Override
	public void process(CommandResultContext commandResultContext) {
		commandResultContext.setResult(commandResultContext.getOrigin());
	}
}
