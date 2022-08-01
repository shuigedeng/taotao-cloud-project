package com.taotao.cloud.sys.biz.modules.tcp.controller;

import java.net.SocketException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.net.HostAndPort;
import org.apache.commons.codec.DecoderException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.sanri.tools.modules.tcp.service.ClientService;
import com.sanri.tools.modules.tcp.service.ServerService;

@RestController
@RequestMapping("/tcp/server")
public class TcpServerController {

    @Autowired
    private ServerService serverService;

    @GetMapping("/open")
    public ServerService.ServerConnectState open(int port,String reciveModel) throws SocketException, InterruptedException {
        serverService.open(port);

        return serverService.state(reciveModel);
    }

    @GetMapping("/close")
    public void close(){
        serverService.close();
    }

    @GetMapping("/state")
    public ClientService.ConnectState state(String reciveModel){
        return serverService.state(reciveModel);
    }

    @GetMapping("/sendCommand")
    public void sendCommand(String ascii,String hex) throws DecoderException {
        serverService.sendCommand(ascii,hex);
    }

    @GetMapping("/clients")
    public List<ServerService.ClientKey> clients(){
        return serverService.clients();
    }

    @PostMapping("/groupSendCommand")
    public void groupSendCommand(@RequestBody GroupSendMessage groupSendMessage) throws DecoderException {
        final List<String> hostAndPorts = groupSendMessage.getHostAndPorts();
        final Set<HostAndPort> collect = hostAndPorts.stream().map(HostAndPort::fromString).collect(Collectors.toSet());
        serverService.sendCommand(collect,groupSendMessage.getAscii(),groupSendMessage.getHex());
    }
}
