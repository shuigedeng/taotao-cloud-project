package com.taotao.cloud.message.biz.austin.handler.handler;

import com.taotao.cloud.message.biz.austin.common.domain.TaskInfo;
import com.taotao.cloud.message.biz.austin.common.enums.AnchorState;
import com.taotao.cloud.message.biz.austin.handler.flowcontrol.FlowControlFactory;
import com.taotao.cloud.message.biz.austin.handler.flowcontrol.FlowControlParam;
import com.taotao.cloud.message.biz.austin.support.utils.LogUtils;
import com.taotao.cloud.message.biz.austin.common.domain.AnchorInfo;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

/**
 * @author 3y
 * 发送各个渠道的handler
 */
public abstract class BaseHandler implements Handler {
	@Autowired
	private HandlerHolder handlerHolder;
	@Autowired
	private LogUtils logUtils;
	@Autowired
	private FlowControlFactory flowControlFactory;

	/**
	 * 标识渠道的Code
	 * 子类初始化的时候指定
	 */
	protected Integer channelCode;

	/**
	 * 限流相关的参数
	 * 子类初始化的时候指定
	 */
	protected FlowControlParam flowControlParam;

	/**
	 * 初始化渠道与Handler的映射关系
	 */
	@PostConstruct
	private void init() {
		handlerHolder.putHandler(channelCode, this);
	}

	/**
	 * 流量控制
	 *
	 * @param taskInfo
	 */
	public void flowControl(TaskInfo taskInfo) {
		// 只有子类指定了限流参数，才需要限流
		if (flowControlParam != null) {
			flowControlFactory.flowControl(taskInfo, flowControlParam);
		}
	}

	@Override
	public void doHandler(TaskInfo taskInfo) {
		flowControl(taskInfo);
		if (handler(taskInfo)) {
			logUtils.print(AnchorInfo.builder().state(AnchorState.SEND_SUCCESS.getCode()).businessId(taskInfo.getBusinessId()).ids(taskInfo.getReceiver()).build());
			return;
		}
		logUtils.print(AnchorInfo.builder().state(AnchorState.SEND_FAIL.getCode()).businessId(taskInfo.getBusinessId()).ids(taskInfo.getReceiver()).build());
	}


	/**
	 * 统一处理的handler接口
	 *
	 * @param taskInfo
	 * @return
	 */
	public abstract boolean handler(TaskInfo taskInfo);


}
