package com.taotao.cloud.ccsr.core.remote.grpc;

import io.grpc.CompressorRegistry;
import io.grpc.DecompressorRegistry;
import io.grpc.Server;
import io.grpc.netty.shaded.io.grpc.netty.NettyServerBuilder;
import org.ohara.msc.common.config.OHaraMcsConfig;
import com.taotao.cloud.ccsr.core.remote.AbstractRpcServer;
import com.taotao.cloud.ccsr.spi.Join;
import com.taotao.cloud.ccsr.spi.SpiExtensionFactory;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author SpringCat
 */
@Join(order = 1, isSingleton = true)
public class GrpcServer extends AbstractRpcServer {

    private Server server;

    private OHaraMcsConfig config;

    public GrpcServer() {
        // 必须要有，用于SPI加载初始化
    }

    public GrpcServer(OHaraMcsConfig config) {
        init(config);
    }

    @Override
    public void init(OHaraMcsConfig config) {
        this.config = config;
    }

    @Override
    public int port() {
        return config.getPort();
    }

    @Override
    public synchronized void startServer() throws IOException {
        OHaraMcsConfig.GrpcConfig grpcConfig = config.getGrpcConfig();
        if (grpcConfig == null) {
            throw new IllegalArgumentException("OHaraMcs gRPC server config can't be null");
        }

        List<GrpcService> services = SpiExtensionFactory.getExtensions(GrpcService.class);
        NettyServerBuilder builder = NettyServerBuilder.forPort(port())
                .compressorRegistry(CompressorRegistry.getDefaultInstance())
                .decompressorRegistry(DecompressorRegistry.getDefaultInstance())
                .maxInboundMessageSize(grpcConfig.getMaxInboundMessageSize())
                .keepAliveTime(grpcConfig.getKeepAliveTime(), TimeUnit.MILLISECONDS)
                .keepAliveTimeout(grpcConfig.getKeepAliveTimeout(), TimeUnit.MILLISECONDS)
                .permitKeepAliveTime(grpcConfig.getPermitKeepAliveTime(), TimeUnit.MILLISECONDS);

        // TODO gRPC允许再服务调用的时候，为每个服务定制责任链拦截器
        services.forEach(builder::addService);

        server = builder.build();
        server.start();
    }

    @Override
    public void stopServer() {
        if (server != null) {
            server.shutdownNow();
        }
    }

    @Override
    public void await() {
        if (server != null) {
            try {
                server.awaitTermination();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
