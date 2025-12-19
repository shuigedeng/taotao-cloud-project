package com.taotao.cloud.message.biz.channels.websockt.original;

import com.taotao.boot.websocket.original.WebsocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * OriginalWebsocketController
 *
 * @author shuigedeng
 * @version 2026.01
 * @since 2025-12-19 09:30:45
 */
@RestController("/original")
public class OriginalWebsocketController {

    @Autowired
    private WebsocketService websocketService;

    @PostMapping("/push")
    public ResponseEntity<String> pushToWeb() {
        websocketService.sendMessageById("", "", "sadfasdf");
        return ResponseEntity.ok("MSG SEND SUCCESS");
    }

    @PostMapping("/pushAll")
    public ResponseEntity<String> pushAll() {
        websocketService.sendMessageAll("", "sadfasdf");
        return ResponseEntity.ok("MSG SEND SUCCESS");
    }

}
