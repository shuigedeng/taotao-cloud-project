package com.taotao.cloud.message.biz.infrastructure.channels.websockt.netty;

import com.baomidou.mybatisplus.extension.service.IService;

public interface ChatService extends IService<Chat> {


	void sendInfo( Chat chat);
}
