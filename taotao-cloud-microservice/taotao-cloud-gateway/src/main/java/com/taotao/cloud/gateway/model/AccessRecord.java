package com.taotao.cloud.gateway.model;

import lombok.*;
import lombok.Data;
import lombok.experimental.*;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.net.URI;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccessRecord implements Serializable {
    private String formData;
    private URI targetUri;
    private String method;
    private String scheme;
    private String path;
    private String body;
    private String ip;
    private Integer status;
    private Long userId;
    private Long consumingTime;
    private LocalDateTime createTime;
}
