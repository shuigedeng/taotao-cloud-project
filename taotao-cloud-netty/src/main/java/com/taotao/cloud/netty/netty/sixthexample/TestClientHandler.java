package com.taotao.cloud.netty.netty.sixthexample;


import com.taotao.cloud.netty.grpc.code2.MyMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Random;

public class TestClientHandler extends SimpleChannelInboundHandler<MyMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MyMessage msg) throws Exception {

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        int randomInt = new Random().nextInt(3);

        MyMessage myMessage = null;

        //if (0 == randomInt) {
        //    myMessage = MyMessage.newBuilder().
        //            setDataType(MyMessage.DataType.PersonType).
        //            setPerson(Person.newBuilder().
        //                    setName("张三").setAge(20).
        //                    setAddress("北京").build()).
        //            build();
        //} else if (1 == randomInt) {
        //    myMessage = MyMessage.newBuilder().
        //            setDataType(MyMessage.DataType.DogType).
        //            setDog(Dog.newBuilder().
        //                    setName("一只狗").setAge(2).
        //                    build()).
        //            build();
        //} else {
        //    myMessage = MyMessage.newBuilder().
        //            setDataType(MyMessage.DataType.CatType).
        //            setCat(Cat.newBuilder().
        //                    setName("一只猫").setCity("上海").
        //                    build()).
        //            build();
        //}

        ctx.channel().writeAndFlush(myMessage);
    }
}
