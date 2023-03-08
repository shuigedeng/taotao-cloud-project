package com.taotao.cloud.sys.biz.config.grpc;

import io.grpc.Status;
import io.grpc.netty.NettyServerBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import net.devh.boot.grpc.server.advice.GrpcAdvice;
import net.devh.boot.grpc.server.advice.GrpcExceptionHandler;
import net.devh.boot.grpc.server.event.GrpcServerShutdownEvent;
import net.devh.boot.grpc.server.event.GrpcServerStartedEvent;
import net.devh.boot.grpc.server.event.GrpcServerTerminatedEvent;
import net.devh.boot.grpc.server.security.check.AccessPredicate;
import net.devh.boot.grpc.server.security.check.AccessPredicateVoter;
import net.devh.boot.grpc.server.security.check.GrpcSecurityMetadataSource;
import net.devh.boot.grpc.server.security.check.ManualGrpcSecurityMetadataSource;
import net.devh.boot.grpc.server.serverfactory.GrpcServerConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.vote.UnanimousBased;
import org.springframework.stereotype.Component;

public class GrpcConfig {

	//注意，根据您的配置，在应用程序上下文中可能有不同类型的 ServerBuilder (例如InProcessServerBuilder)。
	@Bean
	public GrpcServerConfigurer keepAliveServerConfigurer() {
		return serverBuilder -> {
			if (serverBuilder instanceof NettyServerBuilder) {
				((NettyServerBuilder) serverBuilder)
					.keepAliveTime(30, TimeUnit.SECONDS)
					.keepAliveTimeout(5, TimeUnit.SECONDS)
					.permitKeepAliveWithoutCalls(true);
			}
		};
	}

	@Bean
	GrpcSecurityMetadataSource grpcSecurityMetadataSource() {
		final ManualGrpcSecurityMetadataSource source = new ManualGrpcSecurityMetadataSource();
		source.set(MyServiceGrpc.getMethodA(), AccessPredicate.authenticated());
		source.set(MyServiceGrpc.getMethodB(), AccessPredicate.hasRole("ROLE_USER"));
		source.set(MyServiceGrpc.getMethodC(), AccessPredicate.hasAllRole("ROLE_FOO", "ROLE_BAR"));
		source.set(MyServiceGrpc.getMethodD(), (auth, call) -> "admin".equals(auth.getName()));
		source.setDefault(AccessPredicate.denyAll());
		return source;
	}

	@Bean
	AccessDecisionManager accessDecisionManager() {
		final List<AccessDecisionVoter<?>> voters = new ArrayList<>();
		voters.add(new AccessPredicateVoter());
		return new UnanimousBased(voters);
	}

	@GrpcAdvice
	public class GrpcExceptionAdvice {

		@GrpcExceptionHandler
		public Status handleInvalidArgument(IllegalArgumentException e) {
			return Status.INVALID_ARGUMENT.withDescription("Your description").withCause(e);
		}

		@GrpcExceptionHandler(ResourceNotFoundException.class)
		public StatusException handleResourceNotFoundException(ResourceNotFoundException e) {
			Status status = Status.NOT_FOUND.withDescription("Your description").withCause(e);
			Metadata metadata = ...
			return status.asException(metadata);
		}

	}

	@Component
	public class MyEventListenerComponent {

		//此事件将在服务端启动后触发。
		@EventListener
		public void onServerStarted(GrpcServerStartedEvent event) {
			System.out.println(
				"gRPC Server started, listening on address: " + event.getAddress() + ", port: "
					+ event.getPort());
		}

		//此事件将在服务端关闭前触发。 服务端将不再处理新请求。
		@EventListener
		public void onServerStarted(GrpcServerShutdownEvent event) {
			System.out.println(
				"gRPC Server started, listening on address: " + event.getAddress() + ", port: "
					+ event.getPort());
		}

		//此事件将在服务端关闭后触发。 服务端将不再处理任何请求。
		@EventListener
		public void onServerStarted(GrpcServerTerminatedEvent event) {
			System.out.println(
				"gRPC Server started, listening on address: " + event.getAddress() + ", port: "
					+ event.getPort());
		}

	}
}
