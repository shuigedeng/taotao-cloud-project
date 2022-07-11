package com.taotao.cloud.sys.biz.websockt.stomp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class StompWSController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    // @MessageMapping("/hello")
    // @SendTo("/topic/hello")
    // public ResponseMessage hello(RequestMessage requestMessage) {
    //     System.out.println("接收消息：" + requestMessage);
    //     return new ResponseMessage("服务端接收到你发的：" + requestMessage);
    // }

    @GetMapping("/sendMsgByUser")
    public @ResponseBody
    Object sendMsgByUser(String token, String msg) {
        simpMessagingTemplate.convertAndSendToUser(token, "/msg", msg);
        return "success";
    }

    @GetMapping("/sendMsgByAll")
    public @ResponseBody
    Object sendMsgByAll(String msg) {
        simpMessagingTemplate.convertAndSend("/topic", msg);
        return "success";
    }

    @GetMapping("/test")
    public String test() {
        return "test-stomp.html";
    }
}
