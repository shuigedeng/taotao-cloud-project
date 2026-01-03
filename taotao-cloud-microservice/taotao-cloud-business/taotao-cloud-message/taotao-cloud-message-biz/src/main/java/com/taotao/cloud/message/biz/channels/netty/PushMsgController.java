package com.taotao.cloud.message.biz.channels.netty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * PushMsgController
 *
 * @author shuigedeng
 * @version 2026.02
 * @since 2025-12-19 09:30:45
 */
@RestController
@RequestMapping("/push")
public class PushMsgController {

    /**
     * 同时为了方便操作，我们还可以提取出 Controller
     */

    @Autowired
    PushMsgService pushMsgService;

    @RequestMapping("/pushOne")
    public void pushOne( DataContent dataContent ) {
        pushMsgService.pushMsgToOne(dataContent);
    }

    @RequestMapping("/pushAll")
    public void pushAll( DataContent dataContent ) {
        pushMsgService.pushMsgToAll(dataContent);
    }
}

