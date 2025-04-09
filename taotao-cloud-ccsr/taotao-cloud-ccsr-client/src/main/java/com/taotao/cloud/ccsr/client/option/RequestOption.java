package com.taotao.cloud.ccsr.client.option;

import com.google.protobuf.Timestamp;
import lombok.Data;
import com.taotao.cloud.ccsr.api.grpc.auto.MetadataType;
import org.ohara.msc.common.config.OHaraMcsConfig;

import java.util.Map;

@Data
public abstract class RequestOption {

    public abstract String protocol();
}
