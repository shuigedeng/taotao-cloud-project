package com.taotao.cloud.ccsr.client.context;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author shuigedeng
 */
@Data
@NoArgsConstructor
public class CcsrContext {

    private String sign;

    private String md5;

    private String namespace;

    private String configDataString;
}
