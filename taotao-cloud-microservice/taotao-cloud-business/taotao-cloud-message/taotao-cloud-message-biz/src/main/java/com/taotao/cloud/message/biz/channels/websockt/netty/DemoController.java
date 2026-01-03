package com.taotao.cloud.message.biz.channels.websockt.netty;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * DemoController
 *
 * @author shuigedeng
 * @version 2026.02
 * @since 2025-12-19 09:30:45
 */
@RestController
public class DemoController {

    @Autowired
    private ChatService chatService;

    @PostMapping("/push")
    public ResponseEntity<String> pushToWeb( @RequestBody Chat chat ) {
        chat.setCreateTime(LocalDateTime.now());
        chatService.save(chat);
        chatService.sendInfo(chat);

        return ResponseEntity.ok("MSG SEND SUCCESS");
    }

    @GetMapping("/close")
    public String close( String userId ) {
        NettyWebSocket.close(userId);
        return "ok";
    }

    @GetMapping("/getOnlineUser")
    public Map getOnlineUser() {
        return NettyWebSocket.getOnlineUser();
    }

    @GetMapping("/getMessage")
    public ResponseEntity<List<Chat>> getMessage( String userId ) {
        QueryWrapper<Chat> queryWrapper = new QueryWrapper();
        List<Chat> list = chatService.
                list(queryWrapper.lambda().eq(Chat::getTargetUserId, userId).or().eq(Chat::getUserId, userId));
        return ResponseEntity.ok(list);
    }

}
