package com.taotao.cloud.ccsr.client.dto;


import com.taotao.cloud.ccsr.client.listener.ConfigData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.annotation.Annotation;

/**
 * @date 2025-03-26 16:52
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServerAddress implements ConfigData {
    private String host;
    private Integer port;
    private boolean active = true;
}
