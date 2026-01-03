/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.netty.atguigu.netty.codec2;

import com.taotao.cloud.netty.grpc.code2.MyMessage;
import com.taotao.cloud.netty.grpc.code2.Student;
import com.taotao.cloud.netty.grpc.code2.Worker;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.util.Random;

/**
 * NettyClientHandler
 *
 * @author shuigedeng
 * @version 2026.02
 * @since 2025-12-19 09:30:45
 */
public class NettyClientHandler extends ChannelInboundHandlerAdapter {

    // 当通道就绪就会触发该方法
    @Override
    public void channelActive( ChannelHandlerContext ctx ) throws Exception {

        // 随机的发送Student 或者 Workder 对象
        int random = new Random().nextInt(3);
        MyMessage myMessage = null;

        if (0 == random) { // 发送Student 对象

            myMessage =
                    MyMessage.newBuilder()
                            .setDataType(MyMessage.DataType.StudentType)
                            .setStudent(Student.newBuilder().setId(5).setName("玉麒麟 卢俊义").build())
                            .build();
        } else { // 发送一个Worker 对象

            myMessage =
                    MyMessage.newBuilder()
                            .setDataType(MyMessage.DataType.WorkerType)
                            .setWorker(Worker.newBuilder().setAge(20).setName("老李").build())
                            .build();
        }

        ctx.writeAndFlush(myMessage);
    }

    // 当通道有读取事件时，会触发
    @Override
    public void channelRead( ChannelHandlerContext ctx, Object msg ) throws Exception {

        ByteBuf buf = (ByteBuf) msg;
        System.out.println("服务器回复的消息:" + buf.toString(CharsetUtil.UTF_8));
        System.out.println("服务器的地址： " + ctx.channel().remoteAddress());
    }

    @Override
    public void exceptionCaught( ChannelHandlerContext ctx, Throwable cause ) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
