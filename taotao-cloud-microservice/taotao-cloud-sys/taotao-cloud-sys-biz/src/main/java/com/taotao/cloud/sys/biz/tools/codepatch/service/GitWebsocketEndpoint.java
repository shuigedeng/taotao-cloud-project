package com.taotao.cloud.sys.biz.tools.codepatch.service;

import com.taotao.cloud.sys.biz.tools.codepatch.controller.dtos.CompileMessage;
import com.taotao.cloud.sys.biz.tools.core.security.UserService;
import java.io.IOException;
import java.net.InetSocketAddress;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class GitWebsocketEndpoint {

    @Autowired
    private GitService gitService;

    @Autowired(required = false)
    private UserService userService;

    @MessageMapping("/execMavenCommand")
    public void compile(@RequestBody CompileMessage compileMessage, SimpMessageHeaderAccessor simpMessageHeaderAccessor) throws IOException, InterruptedException {
        final InetSocketAddress ip = (InetSocketAddress)simpMessageHeaderAccessor.getSessionAttributes().get("ip");
        gitService.execMavenCommand(ip.getHostName(),compileMessage);
    }
}
