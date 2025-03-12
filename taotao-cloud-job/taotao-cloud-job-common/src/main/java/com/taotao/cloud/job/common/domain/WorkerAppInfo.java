package com.taotao.cloud.job.common.domain;

import lombok.Data;
import lombok.experimental.Accessors;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor

public class WorkerAppInfo implements Serializable {

    /**
     * 应用唯一 ID
     */
    private Long appId;
}
