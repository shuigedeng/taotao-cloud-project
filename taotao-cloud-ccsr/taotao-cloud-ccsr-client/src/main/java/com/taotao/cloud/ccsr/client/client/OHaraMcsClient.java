package com.taotao.cloud.ccsr.client.client;


import com.taotao.cloud.ccsr.api.event.EventType;
import com.taotao.cloud.ccsr.api.grpc.auto.MetadataReadRequest;
import com.taotao.cloud.ccsr.api.grpc.auto.Response;
import org.ohara.msc.client.filter.ConvertFilter;
import org.ohara.msc.client.filter.InvokerFilter;
import org.ohara.msc.client.filter.SignFilter;
import org.ohara.msc.client.filter.ValidationFilter;
import org.ohara.msc.dto.ServerAddress;
import org.ohara.msc.listener.ConfigListener;
import org.ohara.msc.option.GrpcOption;
import org.ohara.msc.option.RequestOption;
import org.ohara.msc.request.Payload;

import java.util.List;
import java.util.ServiceLoader;
import java.util.concurrent.TimeUnit;

public class OHaraMcsClient extends AbstractClient<RequestOption> {

    protected OHaraMcsClient(String namespace) {
        super(namespace);
    }

    @Override
    protected void buildChain() throws Exception {
        addNext(new ValidationFilter<>(this))
                .addNext(new SignFilter<>(this))
                    .addNext(new ConvertFilter<>(this))
                        .addNext(new InvokerFilter(this));
    }


    public static Builder builder(String namespace, RequestOption option) {
        return new Builder(namespace, option);
    }

    public static class Builder extends AbstractBuilder<Builder, OHaraMcsClient> {

        protected Builder(String namespace, RequestOption option) {
            super(namespace, option);
        }

        @Override
        protected OHaraMcsClient create(String namespace) {
            return new OHaraMcsClient(namespace);
        }

    }

    public static void main(String[] args) throws InterruptedException {
        ServiceLoader.load(ConfigListener.class).forEach(ConfigListener::register);

        // 这个客户端全局只加载一次
        GrpcOption option = new GrpcOption();
        option.setServerAddresses(List.of(new ServerAddress("127.0.0.1", 8000, true),
                new ServerAddress("127.0.0.1", 8200, true),
                new ServerAddress("127.0.0.1", 8100, true),
                new ServerAddress("127.0.0.1", 8300, true)
        ));
        OHaraMcsClient mcsClient = OHaraMcsClient.builder("default", option).build();

        Payload payload = Payload.builder().build();
        payload.setConfigData(new ServerAddress("127.0.0.3", 8000, true));
        payload.setNamespace("default");
        payload.setGroup("default_group");
        payload.setDataId("default_data_id");
//        for (int i = 0; i < 10; i++) {
            Response response = mcsClient.request(payload);
            System.out.println(response);
//        }


        mcsClient.destroy(15, TimeUnit.SECONDS);
    }
}
