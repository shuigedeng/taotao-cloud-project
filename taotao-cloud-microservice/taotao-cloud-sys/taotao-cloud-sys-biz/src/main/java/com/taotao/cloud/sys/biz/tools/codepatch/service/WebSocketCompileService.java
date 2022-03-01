package com.taotao.cloud.sys.biz.tools.codepatch.service;//package com.sanri.tools.modules.codepatch.service;
//
//import com.alibaba.fastjson.JSON;
//import lombok.Data;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.beans.BeansException;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.ApplicationContextAware;
//import org.springframework.stereotype.Component;
//
//import javax.websocket.*;
//import javax.websocket.server.PathParam;
//import javax.websocket.server.ServerEndpoint;
//import java.io.IOException;
//import java.net.InetSocketAddress;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//
//@Slf4j
//@ServerEndpoint(value = "/compile/message/{id}")
//@Component
//public class WebSocketCompileService {
//    private static final Map<String,Session> sessionMap = new ConcurrentHashMap<>();
//
//    private static GitService gitService;
//
//    /**
//     * 连接建立成功调用的方法
//     */
//    @OnOpen
//    public void onOpen(Session session,@PathParam("id") String id) {
//        log.info("打开 session 连接:{}",id);
//        sessionMap.put(id,session);
//    }
//
//    /**
//     * 连接关闭调用的方法
//     */
//    @OnClose
//    public void onClose(@PathParam("id") String id) {
//        log.info("关闭 session 连接:{}",id);
//        sessionMap.remove(id);
//    }
//
//    @Data
//    public static final class CompileMessage{
//        private String group;
//        private String repository;
//        private String websocketId;
//        private String relativePath;
//    }
//
//    @OnMessage
//    public void onMessage(String message,Session session) throws IOException, InterruptedException {
//        final CompileMessage compileMessage = JSON.parseObject(message, CompileMessage.class);
//        final InetSocketAddress remoteAddress = WebsocketUtil.getRemoteAddress(session);
//        try {
//            gitService.compile(remoteAddress.getHostName(), compileMessage.getWebsocketId(), compileMessage.getGroup(), compileMessage.getRepository(), compileMessage.getRelativePath());
//        }catch (Exception e){
//            log.error("编译时出错: {}",e.getMessage(),e);
//            sendMessage(compileMessage.websocketId, (e instanceof NullPointerException) ? "编译时发生异常": e.getMessage());
//        }
//    }
//
//    @OnError
//    public void onError(Session session, Throwable error) {
//        log.error("发生错误",error);
//    }
//
//    /**
//     * 服务端发送消息给客户端
//     */
//    public void sendMessage(String id,String message) {
//        final Session session = sessionMap.get(id);
//        if (session != null && session.isOpen()) {
//            try {
//                log.info("服务端给客户端[{}]发送消息{}", id, message);
//                synchronized (session) {
//                    session.getBasicRemote().sendText(message);
//                }
//            } catch (Exception e) {
//                log.error("服务端发送消息给客户端失败：{}", e);
//            }
//        }else{
//            log.error("session 已经关闭, 发送消息失败:{}",message);
//        }
//
//    }
//
//    @Autowired
//    public void setGitService(GitService gitService) {
//        WebSocketCompileService.gitService = gitService;
//    }
//}
