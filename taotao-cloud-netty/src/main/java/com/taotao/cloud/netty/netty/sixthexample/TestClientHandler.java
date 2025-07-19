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

package com.taotao.cloud.netty.netty.sixthexample;

import com.taotao.cloud.netty.grpc.code2.MyMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import java.util.Random;

public class TestClientHandler extends SimpleChannelInboundHandler<MyMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MyMessage msg) throws Exception {}

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        int randomInt = new Random().nextInt(3);

        MyMessage myMessage = null;

        // if (0 == randomInt) {
        //    myMessage = MyMessage.newBuilder().
        //            setDataType(MyMessage.DataType.PersonType).
        //            setPerson(Person.newBuilder().
        //                    setName("张三").setAge(20).
        //                    setAddress("北京").build()).
        //            build();
        // } else if (1 == randomInt) {
        //    myMessage = MyMessage.newBuilder().
        //            setDataType(MyMessage.DataType.DogType).
        //            setDog(Dog.newBuilder().
        //                    setName("一只狗").setAge(2).
        //                    build()).
        //            build();
        // } else {
        //    myMessage = MyMessage.newBuilder().
        //            setDataType(MyMessage.DataType.CatType).
        //            setCat(Cat.newBuilder().
        //                    setName("一只猫").setCity("上海").
        //                    build()).
        //            build();
        // }

        ctx.channel().writeAndFlush(myMessage);
    }
}
