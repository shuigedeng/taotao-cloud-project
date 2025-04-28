package com.taotao.cloud.ccsr.client.client.invoke;

import com.google.protobuf.Message;
import com.google.protobuf.Timestamp;
import com.taotao.cloud.ccsr.api.event.EventType;
import com.taotao.cloud.ccsr.client.client.CcsrClient;
import com.taotao.cloud.ccsr.client.context.CcsrContext;
import com.taotao.cloud.ccsr.client.option.GrpcOption;
import com.taotao.cloud.ccsr.client.remote.RpcClient;
import com.taotao.cloud.ccsr.client.request.Payload;
import com.taotao.cloud.ccsr.common.enums.RaftGroup;
import com.taotao.cloud.ccsr.common.exception.InitializationException;
import com.taotao.cloud.ccsr.common.exception.CcsrClientException;
import com.taotao.cloud.ccsr.spi.SpiExtensionFactory;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import com.taotao.cloud.ccsr.client.remote.grpc.GrpcClient;
public class GrpcInvoker extends AbstractInvoker<Message, GrpcOption> {

	private final GrpcClient grpcClient;

	public GrpcInvoker(CcsrClient client) {
		super(client);
		GrpcOption grpcOption = (GrpcOption) client.getOption();
		if (grpcOption == null) {
			throw new InitializationException("Init Grpc Invoker fail, GrpcOption is empty.");
		}
		if (StringUtils.isBlank(client.getNamespace())) {
			throw new IllegalArgumentException("Init Grpc Invoker fail, Namespace is null.");
		}
		if (CollectionUtils.isEmpty(grpcOption.getServerAddresses())) {
			throw new IllegalArgumentException("Init Grpc Invoker fail, ServerAddresses is empty.");
		}
		grpcClient = (GrpcClient) SpiExtensionFactory.getExtension(protocol(), RpcClient.class);
		grpcClient.init(client.getNamespace(), grpcOption.getServerAddresses());
	}

	public Response innerInvoke(Message request, EventType eventType) {
		return switch (eventType) {
			case PUT -> grpcClient.put((MetadataWriteRequest) request);
			case DELETE -> grpcClient.delete((MetadataDeleteRequest) request);
			case GET -> grpcClient.get((MetadataReadRequest) request);
			default -> throw new IllegalArgumentException("Unsupported event type: " + eventType);
		};
	}

	@Override
	public Response invoke(CcsrContext context, Payload request) {
		GrpcOption option = getOption();
		Message message = convert(context, option, request);
		return innerInvoke(message, request.getEventType());
	}

	@Override
	public String protocol() {
		return "grpc";
	}

	@Override
	public Message convert(CcsrContext context, GrpcOption option, Payload request) {
		return switch (request.getEventType()) {
			case PUT -> MetadataWriteRequest.newBuilder()
				.setRaftGroup(RaftGroup.CONFIG_CENTER_GROUP.getName())
				.setNamespace(context.getNamespace())
				.setGroup(request.getGroup())
				.setTag(request.getTag())
				.setDataId(request.getDataId())
				.setMetadata(buildMetadata(context, option, request))
				.build();
			case DELETE -> MetadataDeleteRequest.newBuilder()
				.setRaftGroup(RaftGroup.CONFIG_CENTER_GROUP.getName())
				.setNamespace(context.getNamespace())
				.setGroup(request.getGroup())
				.setTag(request.getTag())
				.setDataId(request.getDataId())
				.build();
			case GET -> MetadataReadRequest.newBuilder()
				.setRaftGroup(RaftGroup.CONFIG_CENTER_GROUP.getName())
				.setNamespace(context.getNamespace())
				.setGroup(request.getGroup())
				.setTag(request.getTag())
				.setDataId(request.getDataId())
				.build();
			default -> throw new IllegalArgumentException("Unsupported event type: " + request.getEventType());
		};

	}

	private Metadata buildMetadata(CcsrContext context, GrpcOption option, Payload request) {
		return Metadata.newBuilder()
			.setNamespace(context.getNamespace())
			.setGroup(request.getGroup())
			.setTag(request.getTag())
			.setDataId(request.getDataId())
			.setDataKey(request.getConfigData().key())
			.setType(request.getType())
			.setContent(context.getConfigDataString())
			.setMd5(context.getMd5())
			.setGmtCreate(Timestamp.newBuilder().setSeconds(request.getGmtCreate()))
			.setGmtModified(Timestamp.newBuilder().setSeconds(request.getGmtModified()))
//                .putAllExt(request.getExt())
			.build();
	}

	@Override
	public void shutdown() throws CcsrClientException {
		grpcClient.shutdown();
	}
}
