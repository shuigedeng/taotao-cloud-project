package com.taotao.cloud.netty.protobuf;


import com.taotao.cloud.netty.grpc.code2.Student;

public class ProtoBufTest {

    public static void main(String[] args) throws Exception {
        Student student = Student.newBuilder().
                setName("张三").setAge(20).setAddress("北京").build();

        byte[] student2ByteArray = student.toByteArray();

        Student student2 = Student.parseFrom(student2ByteArray);

        System.out.println(student2.getName());
        System.out.println(student2.getAge());
        System.out.println(student2.getAddress());
    }
}
