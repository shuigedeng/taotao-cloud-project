package com.taotao.cloud.ccsr.client.context;

import lombok.Data;
import lombok.NoArgsConstructor;
import com.taotao.cloud.ccsr.api.event.EventType;

/**
 * @author shuigedeng
 */
@Data
@NoArgsConstructor
public class OHaraMcsContext {

    private String sign;

    private String md5;

    private String namespace;

    private String configDataString;
}
