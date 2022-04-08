package com.taotao.cloud.order.biz.service.order;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.order.api.vo.order.OrderComplaintCommunicationSearchParams;
import com.taotao.cloud.order.api.vo.order.OrderComplaintCommunicationVO;
import com.taotao.cloud.order.biz.entity.order.OrderComplaintCommunication;

/**
 * 订单投诉通信业务层
 **/
public interface OrderComplaintCommunicationService extends IService<OrderComplaintCommunication> {

    /**
     * 添加订单投诉通信
     *
     * @param complainId id
     * @param content 内容
     * @return 状态
     */
    Boolean addCommunication(Long complainId, String content);

    /**
     * 获取通信记录
     *
     * @param searchParams 参数
     * @param pageVO       分页
     * @return
     */
    IPage<OrderComplaintCommunication> getCommunication(
	    OrderComplaintCommunicationSearchParams searchParams, PageVO pageVO);


}
