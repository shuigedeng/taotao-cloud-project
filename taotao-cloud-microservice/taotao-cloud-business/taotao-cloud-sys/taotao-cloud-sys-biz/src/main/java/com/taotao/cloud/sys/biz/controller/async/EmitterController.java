package com.taotao.cloud.sys.biz.controller.async;

import java.io.IOException;
import java.time.LocalTime;
import java.util.Date;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.PrintWriter;
import java.util.concurrent.CompletableFuture;

/**
 * EmitterController
 *
 * @author shuigedeng
 * @version 2026.01
 * @since 2025-12-19 09:30:45
 */
public class EmitterController {

    //ResponseBodyEmitterResponseBodyEmitter适用于需要动态生成内容并逐步发送给客户端的场景，例如文件上传进度、实时日志等。它可以在任务执行过程中逐步向客户端发送更新，而不必等待整个任务完成。这种方式通过Servlet 3.0的异步处理能力实现，不会阻塞Servlet的请求线程，从而提高了系统的并发性能和响应能力。
    //优缺点优点：可以动态生成内容并逐步发送给客户端，交互体验好。缺点：需要处理连接超时和异常结束的问题。
    //应用场景ResponseBodyEmitter适用于需要实时反馈处理进度的场景，如文件上传、实时日志推送等。
    @GetMapping("/bodyEmitter")
    public ResponseBodyEmitter handle() {
        // 创建一个ResponseBodyEmitter，-1代表不超时
        ResponseBodyEmitter emitter = new ResponseBodyEmitter(-1L);
        // 异步执行耗时操作
        CompletableFuture.runAsync(() -> {
            try {
                // 模拟耗时操作
                for (int i = 0; i < 10000; i++) {
                    System.out.println("bodyEmitter " + i);
                    // 发送数据
                    emitter.send("bodyEmitter " + i + " @ " + new Date() + "\n");
                    Thread.sleep(2000);
                }
                // 完成
                emitter.complete();
            } catch (Exception e) {
                // 发生异常时结束接口
                emitter.completeWithError(e);
            }
        });
        return emitter;
    }

    //SseEmitter是ResponseBodyEmitter的一个子类，它主要用于服务器向客户端推送实时数据，如实时消息推送、状态更新等。它利用Server-Sent Events（SSE）技术在服务器和客户端之间打开一个单向通道，服务端响应的不再是一次性的数据包，而是text/event-stream类型的数据流信息，在有数据变更时从服务器流式传输到客户端。
    //优缺点优点：能够实时推送数据到客户端，实现低延迟的实时通信。缺点：单向通信，客户端无法主动发送数据到服务器；需要处理连接断开和重连的问题。
    //应用场景SseEmitter适用于需要实时推送数据的场景，如实时消息推送、股票价格更新等。
    @GetMapping("/subSseEmitter/{userId}")
    public SseEmitter sseEmitter( @PathVariable String userId ) {
        // 创建一个SseEmitter，-1代表不超时
        SseEmitter emitter = new SseEmitter(-1L);
        // 异步执行耗时操作
        CompletableFuture.runAsync(() -> {
            try {
                // 模拟耗时操作并推送数据
                for (int i = 0; i < 10; i++) {
                    SseEmitter.SseEventBuilder event = SseEmitter.event()
                            .data("sseEmitter " + userId + " @ " + LocalTime.now()).id(String.valueOf(userId))
                            .name("sseEmitter");
                    emitter.send(event);
                    Thread.sleep(2000);
                }
                // 完成
                emitter.complete();
            } catch (Exception e) {
                // 发生异常时结束接口
                emitter.completeWithError(e);
            }
        });
        return emitter;
    }


    //StreamingResponseBody用于返回一个StreamingResponseBody对象，客户端可以通过该对象以流的方式逐步读取数据。它同样利用了Servlet 3.0的异步处理能力，允许在后台线程中逐步生成数据并发送给客户端，而不会阻塞Servlet的请求线程。
    //优缺点优点：能够以流的方式逐步返回数据，适用于大数据量的传输。缺点：需要手动管理流的关闭和异常处理。
    //应用场景StreamingResponseBody适用于需要传输大数据量的场景，如文件下载、视频流播放等。
    @GetMapping("/streaming")
    public StreamingResponseBody streaming() {
        return outputStream -> {
            try (PrintWriter writer = new PrintWriter(outputStream)) {
                // 模拟耗时操作并写入数据
                for (int i = 0; i < 100; i++) {
                    writer.println("Streaming data " + i);
                    writer.flush();
                    Thread.sleep(500);
                }
            } catch (InterruptedException e) {
                // 处理异常
                e.printStackTrace();
            }
        };
    }
}
