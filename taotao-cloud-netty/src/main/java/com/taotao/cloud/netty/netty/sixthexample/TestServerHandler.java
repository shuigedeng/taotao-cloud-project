package com.taotao.cloud.netty.netty.sixthexample;


import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import com.taotao.cloud.netty.grpc.code2.MyMessage;
public class TestServerHandler extends SimpleChannelInboundHandler<MyMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MyMessage msg) throws Exception {
        MyMessage.DataType dataType = msg.getDataType();

        //if (dataType == MyMessage.DataType.StudentType) {
        //    Person person = msg.getPerson();
		//
        //    System.out.println(person.getName());
        //    System.out.println(person.getAge());
        //    System.out.println(person.getAddress());
        //} else if (dataType == MyMessage.DataType.DogType) {
        //    Dog dog = msg.getDog();
		//
        //    System.out.println(dog.getName());
        //    System.out.println(dog.getAge());
        //} else {
        //    Cat cat = msg.getCat();
		//
        //    System.out.println(cat.getName());
        //    System.out.println(cat.getCity());
        //}
    }
}
