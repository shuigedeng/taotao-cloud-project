package com.taotao.cloud.sys.biz.modules.tcp.controller;

import com.google.common.net.HostAndPort;
import com.taotao.cloud.sys.biz.modules.core.exception.ToolException;
import com.taotao.cloud.sys.biz.modules.tcp.service.ClientService;
import org.apache.commons.codec.DecoderException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tcp/client")
public class TcpClientController {

    @Autowired
    private ClientService clientService;

    @GetMapping("/open")
    public ClientService.ConnectState open(String host, int port,String reciveModel) throws InterruptedException {
        clientService.open(HostAndPort.fromParts(host,port));
        return clientService.state(reciveModel);
    }

    @GetMapping("/close")
    public void close(){
        clientService.close();
    }

    @GetMapping("/state")
    public ClientService.ConnectState state(String reciveModel){
        return clientService.state(reciveModel);
    }

    /**
     * 发送消息 ,等待返回 10 s 超时
     * @param ascii
     * @param hex
     * @return
     * @throws DecoderException
     * @throws InterruptedException
     */
    @GetMapping("/sendCommand")
    public String sendCommand(String ascii,String hex) throws DecoderException, InterruptedException {
        try {
            return clientService.sendMessage(ascii, hex);
        }catch (InterruptedException e){
            throw new ToolException("发送命令超时:"+e.getMessage());
        }
    }
}
