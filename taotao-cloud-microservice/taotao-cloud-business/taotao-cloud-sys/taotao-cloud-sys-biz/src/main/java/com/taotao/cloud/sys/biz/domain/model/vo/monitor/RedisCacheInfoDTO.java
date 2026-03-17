package com.taotao.cloud.sys.biz.domain.model.vo.monitor;

import lombok.Data;

import java.util.List;
import java.util.Properties;


/**
 * RedisCacheInfoDTO
 *
 * @author shuigedeng
 * @version 2026.04
 * @since 2025-12-19 09:30:45
 */
@Data
public class RedisCacheInfoDTO {

    private Properties info;
    private Object dbSize;
    private List<CommonStatusDTO> commandStats;

    @Data
    public static class CommonStatusDTO {

        private String name;
        private String value;
    }

}
