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

package com.taotao.cloud.netty.grpc;

public class StudentServiceImpl {

    //    @Override
    //    public void getRealNameByUsername(MyRequest request, StreamObserver<MyResponse>
    // responseObserver) {
    //        System.out.println("接受到客户端信息： " + request.getUsername());
    //
    //        responseObserver.onNext(MyResponse.newBuilder().setRealname("张三").build());
    //        responseObserver.onCompleted();
    //    }
    //
    //    @Override
    //    public void getStudentsByAge(StudentRequest request, StreamObserver<StudentResponse>
    // responseObserver) {
    //        System.out.println("接受到客户端信息： " + request.getAge());
    //
    //
    // responseObserver.onNext(StudentResponse.newBuilder().setName("张三").setAge(20).setCity("北京").build());
    //
    // responseObserver.onNext(StudentResponse.newBuilder().setName("李四").setAge(30).setCity("天津").build());
    //
    // responseObserver.onNext(StudentResponse.newBuilder().setName("王五").setAge(40).setCity("成都").build());
    //
    // responseObserver.onNext(StudentResponse.newBuilder().setName("赵六").setAge(50).setCity("深圳").build());
    //
    //        responseObserver.onCompleted();
    //    }
    //
    //    @Override
    //    public StreamObserver<StudentRequest>
    // getStudentsWrapperByAges(StreamObserver<StudentResponseList> responseObserver) {
    //        return new StreamObserver<StudentRequest>() {
    //            @Override
    //            public void onNext(StudentRequest value) {
    //                System.out.println("onNext: " + value.getAge());
    //            }
    //
    //            @Override
    //            public void onError(Throwable t) {
    //                System.out.println(t.getMessage());
    //            }
    //
    //            @Override
    //            public void onCompleted() {
    //                StudentResponse studentResponse =
    // StudentResponse.newBuilder().setName("张三").setAge(20).setCity("西安").build();
    //                StudentResponse studentResponse2 =
    // StudentResponse.newBuilder().setName("李四").setAge(30).setCity("广州").build();
    //
    //                StudentResponseList studentResponseList = StudentResponseList.newBuilder().
    //
    // addStudentResponse(studentResponse).addStudentResponse(studentResponse2).build();
    //
    //                responseObserver.onNext(studentResponseList);
    //                responseObserver.onCompleted();
    //            }
    //        };
    //    }
    //
    //    @Override
    //    public StreamObserver<StreamRequest> biTalk(StreamObserver<StreamResponse>
    // responseObserver) {
    //        return new StreamObserver<StreamRequest>() {
    //            @Override
    //            public void onNext(StreamRequest value) {
    //                System.out.println(value.getRequestInfo());
    //
    //
    // responseObserver.onNext(StreamResponse.newBuilder().setResponseInfo(UUID.randomUUID().toString()).build());
    //            }
    //
    //            @Override
    //            public void onError(Throwable t) {
    //                System.out.println(t.getMessage());
    //            }
    //
    //            @Override
    //            public void onCompleted() {
    //                responseObserver.onCompleted();
    //            }
    //        };
    //    }
}
