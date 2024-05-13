package com.taotao.cloud.mq.common.dto.req;


import com.taotao.cloud.mq.common.dto.req.component.MqConsumerUpdateStatusDto;
import java.util.List;

/**
 * 批量更新状态入参
 *
 * @author shuigedeng
 * @since 2024.05
 */
public class MqConsumerUpdateStatusBatchReq extends MqCommonReq {

    private List<MqConsumerUpdateStatusDto> statusList;

    public List<MqConsumerUpdateStatusDto> getStatusList() {
        return statusList;
    }

    public void setStatusList(List<MqConsumerUpdateStatusDto> statusList) {
        this.statusList = statusList;
    }
}
