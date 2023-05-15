package com.taotao.cloud.message.biz.websockt.netty;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.service.IService;
import lombok.Data;

import java.time.LocalDateTime;

public interface ChatService extends IService<Chat> {


	void sendInfo(Chat chat);
}
