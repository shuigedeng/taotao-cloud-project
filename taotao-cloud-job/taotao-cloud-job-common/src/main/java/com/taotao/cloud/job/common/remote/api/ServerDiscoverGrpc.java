package com.taotao.cloud.job.common.remote.api;

import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;

/**
 * <pre>
 *定义一个类
 * </pre>
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.6.1)",
    comments = "Source: causa/cgrpc.proto")
public final class ServerDiscoverGrpc {

  private ServerDiscoverGrpc() {}

  public static final String SERVICE_NAME = "ServerDiscover";

  // Static method descriptors that strictly reflect the proto.
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<com.taotao.cloud.remote.protos.ServerDiscoverCausa.AppName,
      com.taotao.cloud.remote.protos.CommonCausa.Response> METHOD_ASSERT_APP =
      io.grpc.MethodDescriptor.<com.taotao.cloud.remote.protos.ServerDiscoverCausa.AppName, com.taotao.cloud.remote.protos.CommonCausa.Response>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
          .setFullMethodName(generateFullMethodName(
              "ServerDiscover", "assertApp"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.taotao.cloud.remote.protos.ServerDiscoverCausa.AppName.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.taotao.cloud.remote.protos.CommonCausa.Response.getDefaultInstance()))
          .build();
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<com.taotao.cloud.remote.protos.ServerDiscoverCausa.HeartbeatCheck,
      com.taotao.cloud.remote.protos.CommonCausa.Response> METHOD_HEARTBEAT_CHECK =
      io.grpc.MethodDescriptor.<com.taotao.cloud.remote.protos.ServerDiscoverCausa.HeartbeatCheck, com.taotao.cloud.remote.protos.CommonCausa.Response>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
          .setFullMethodName(generateFullMethodName(
              "ServerDiscover", "heartbeatCheck"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.taotao.cloud.remote.protos.ServerDiscoverCausa.HeartbeatCheck.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.taotao.cloud.remote.protos.CommonCausa.Response.getDefaultInstance()))
          .build();
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<com.taotao.cloud.remote.protos.ServerDiscoverCausa.Ping,
      com.taotao.cloud.remote.protos.CommonCausa.Response> METHOD_PING_SERVER =
      io.grpc.MethodDescriptor.<com.taotao.cloud.remote.protos.ServerDiscoverCausa.Ping, com.taotao.cloud.remote.protos.CommonCausa.Response>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
          .setFullMethodName(generateFullMethodName(
              "ServerDiscover", "pingServer"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.taotao.cloud.remote.protos.ServerDiscoverCausa.Ping.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.taotao.cloud.remote.protos.CommonCausa.Response.getDefaultInstance()))
          .build();
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<com.taotao.cloud.remote.protos.ServerDiscoverCausa.ServerChangeReq,
      com.taotao.cloud.remote.protos.CommonCausa.Response> METHOD_SERVER_CHANGE =
      io.grpc.MethodDescriptor.<com.taotao.cloud.remote.protos.ServerDiscoverCausa.ServerChangeReq, com.taotao.cloud.remote.protos.CommonCausa.Response>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
          .setFullMethodName(generateFullMethodName(
              "ServerDiscover", "serverChange"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.taotao.cloud.remote.protos.ServerDiscoverCausa.ServerChangeReq.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.taotao.cloud.remote.protos.CommonCausa.Response.getDefaultInstance()))
          .build();

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static ServerDiscoverStub newStub(io.grpc.Channel channel) {
    return new ServerDiscoverStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static ServerDiscoverBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new ServerDiscoverBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static ServerDiscoverFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new ServerDiscoverFutureStub(channel);
  }

  /**
   * <pre>
   *定义一个类
   * </pre>
   */
  public static abstract class ServerDiscoverImplBase implements io.grpc.BindableService {

    /**
     */
    public void assertApp(com.taotao.cloud.remote.protos.ServerDiscoverCausa.AppName request,
        io.grpc.stub.StreamObserver<com.taotao.cloud.remote.protos.CommonCausa.Response> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_ASSERT_APP, responseObserver);
    }

    /**
     */
    public void heartbeatCheck(com.taotao.cloud.remote.protos.ServerDiscoverCausa.HeartbeatCheck request,
        io.grpc.stub.StreamObserver<com.taotao.cloud.remote.protos.CommonCausa.Response> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_HEARTBEAT_CHECK, responseObserver);
    }

    /**
     */
    public void pingServer(com.taotao.cloud.remote.protos.ServerDiscoverCausa.Ping request,
        io.grpc.stub.StreamObserver<com.taotao.cloud.remote.protos.CommonCausa.Response> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_PING_SERVER, responseObserver);
    }

    /**
     */
    public void serverChange(com.taotao.cloud.remote.protos.ServerDiscoverCausa.ServerChangeReq request,
        io.grpc.stub.StreamObserver<com.taotao.cloud.remote.protos.CommonCausa.Response> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_SERVER_CHANGE, responseObserver);
    }

    @Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            METHOD_ASSERT_APP,
            asyncUnaryCall(
              new MethodHandlers<
                com.taotao.cloud.remote.protos.ServerDiscoverCausa.AppName,
                com.taotao.cloud.remote.protos.CommonCausa.Response>(
                  this, METHODID_ASSERT_APP)))
          .addMethod(
            METHOD_HEARTBEAT_CHECK,
            asyncUnaryCall(
              new MethodHandlers<
                com.taotao.cloud.remote.protos.ServerDiscoverCausa.HeartbeatCheck,
                com.taotao.cloud.remote.protos.CommonCausa.Response>(
                  this, METHODID_HEARTBEAT_CHECK)))
          .addMethod(
            METHOD_PING_SERVER,
            asyncUnaryCall(
              new MethodHandlers<
                com.taotao.cloud.remote.protos.ServerDiscoverCausa.Ping,
                com.taotao.cloud.remote.protos.CommonCausa.Response>(
                  this, METHODID_PING_SERVER)))
          .addMethod(
            METHOD_SERVER_CHANGE,
            asyncUnaryCall(
              new MethodHandlers<
                com.taotao.cloud.remote.protos.ServerDiscoverCausa.ServerChangeReq,
                com.taotao.cloud.remote.protos.CommonCausa.Response>(
                  this, METHODID_SERVER_CHANGE)))
          .build();
    }
  }

  /**
   * <pre>
   *定义一个类
   * </pre>
   */
  public static final class ServerDiscoverStub extends io.grpc.stub.AbstractStub<ServerDiscoverStub> {
    private ServerDiscoverStub(io.grpc.Channel channel) {
      super(channel);
    }

    private ServerDiscoverStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @Override
    protected ServerDiscoverStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new ServerDiscoverStub(channel, callOptions);
    }

    /**
     */
    public void assertApp(com.taotao.cloud.remote.protos.ServerDiscoverCausa.AppName request,
        io.grpc.stub.StreamObserver<com.taotao.cloud.remote.protos.CommonCausa.Response> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_ASSERT_APP, getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void heartbeatCheck(com.taotao.cloud.remote.protos.ServerDiscoverCausa.HeartbeatCheck request,
        io.grpc.stub.StreamObserver<com.taotao.cloud.remote.protos.CommonCausa.Response> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_HEARTBEAT_CHECK, getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void pingServer(com.taotao.cloud.remote.protos.ServerDiscoverCausa.Ping request,
        io.grpc.stub.StreamObserver<com.taotao.cloud.remote.protos.CommonCausa.Response> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_PING_SERVER, getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void serverChange(com.taotao.cloud.remote.protos.ServerDiscoverCausa.ServerChangeReq request,
        io.grpc.stub.StreamObserver<com.taotao.cloud.remote.protos.CommonCausa.Response> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_SERVER_CHANGE, getCallOptions()), request, responseObserver);
    }
  }

  /**
   * <pre>
   *定义一个类
   * </pre>
   */
  public static final class ServerDiscoverBlockingStub extends io.grpc.stub.AbstractStub<ServerDiscoverBlockingStub> {
    private ServerDiscoverBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private ServerDiscoverBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @Override
    protected ServerDiscoverBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new ServerDiscoverBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.taotao.cloud.remote.protos.CommonCausa.Response assertApp(com.taotao.cloud.remote.protos.ServerDiscoverCausa.AppName request) {
      return blockingUnaryCall(
          getChannel(), METHOD_ASSERT_APP, getCallOptions(), request);
    }

    /**
     */
    public com.taotao.cloud.remote.protos.CommonCausa.Response heartbeatCheck(com.taotao.cloud.remote.protos.ServerDiscoverCausa.HeartbeatCheck request) {
      return blockingUnaryCall(
          getChannel(), METHOD_HEARTBEAT_CHECK, getCallOptions(), request);
    }

    /**
     */
    public com.taotao.cloud.remote.protos.CommonCausa.Response pingServer(com.taotao.cloud.remote.protos.ServerDiscoverCausa.Ping request) {
      return blockingUnaryCall(
          getChannel(), METHOD_PING_SERVER, getCallOptions(), request);
    }

    /**
     */
    public com.taotao.cloud.remote.protos.CommonCausa.Response serverChange(com.taotao.cloud.remote.protos.ServerDiscoverCausa.ServerChangeReq request) {
      return blockingUnaryCall(
          getChannel(), METHOD_SERVER_CHANGE, getCallOptions(), request);
    }
  }

  /**
   * <pre>
   *定义一个类
   * </pre>
   */
  public static final class ServerDiscoverFutureStub extends io.grpc.stub.AbstractStub<ServerDiscoverFutureStub> {
    private ServerDiscoverFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private ServerDiscoverFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @Override
    protected ServerDiscoverFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new ServerDiscoverFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.taotao.cloud.remote.protos.CommonCausa.Response> assertApp(
        com.taotao.cloud.remote.protos.ServerDiscoverCausa.AppName request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_ASSERT_APP, getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.taotao.cloud.remote.protos.CommonCausa.Response> heartbeatCheck(
        com.taotao.cloud.remote.protos.ServerDiscoverCausa.HeartbeatCheck request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_HEARTBEAT_CHECK, getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.taotao.cloud.remote.protos.CommonCausa.Response> pingServer(
        com.taotao.cloud.remote.protos.ServerDiscoverCausa.Ping request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_PING_SERVER, getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.taotao.cloud.remote.protos.CommonCausa.Response> serverChange(
        com.taotao.cloud.remote.protos.ServerDiscoverCausa.ServerChangeReq request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_SERVER_CHANGE, getCallOptions()), request);
    }
  }

  private static final int METHODID_ASSERT_APP = 0;
  private static final int METHODID_HEARTBEAT_CHECK = 1;
  private static final int METHODID_PING_SERVER = 2;
  private static final int METHODID_SERVER_CHANGE = 3;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final ServerDiscoverImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(ServerDiscoverImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_ASSERT_APP:
          serviceImpl.assertApp((com.taotao.cloud.remote.protos.ServerDiscoverCausa.AppName) request,
              (io.grpc.stub.StreamObserver<com.taotao.cloud.remote.protos.CommonCausa.Response>) responseObserver);
          break;
        case METHODID_HEARTBEAT_CHECK:
          serviceImpl.heartbeatCheck((com.taotao.cloud.remote.protos.ServerDiscoverCausa.HeartbeatCheck) request,
              (io.grpc.stub.StreamObserver<com.taotao.cloud.remote.protos.CommonCausa.Response>) responseObserver);
          break;
        case METHODID_PING_SERVER:
          serviceImpl.pingServer((com.taotao.cloud.remote.protos.ServerDiscoverCausa.Ping) request,
              (io.grpc.stub.StreamObserver<com.taotao.cloud.remote.protos.CommonCausa.Response>) responseObserver);
          break;
        case METHODID_SERVER_CHANGE:
          serviceImpl.serverChange((com.taotao.cloud.remote.protos.ServerDiscoverCausa.ServerChangeReq) request,
              (io.grpc.stub.StreamObserver<com.taotao.cloud.remote.protos.CommonCausa.Response>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @Override
    @SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static final class ServerDiscoverDescriptorSupplier implements io.grpc.protobuf.ProtoFileDescriptorSupplier {
    @Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.taotao.cloud.remote.api.CausaGrpcClientGen.getDescriptor();
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (ServerDiscoverGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new ServerDiscoverDescriptorSupplier())
              .addMethod(METHOD_ASSERT_APP)
              .addMethod(METHOD_HEARTBEAT_CHECK)
              .addMethod(METHOD_PING_SERVER)
              .addMethod(METHOD_SERVER_CHANGE)
              .build();
        }
      }
    }
    return result;
  }
}
