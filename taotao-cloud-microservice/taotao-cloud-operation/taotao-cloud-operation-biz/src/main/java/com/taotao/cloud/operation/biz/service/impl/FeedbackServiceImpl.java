package com.taotao.cloud.operation.biz.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.operation.biz.model.entity.Feedback;
import com.taotao.cloud.operation.biz.mapper.FeedbackMapper;
import com.taotao.cloud.operation.biz.service.FeedbackService;
import org.springframework.stereotype.Service;

/**
 * 意见反馈业务层实现
 */
@Service
public class FeedbackServiceImpl extends ServiceImpl<FeedbackMapper, Feedback> implements
	FeedbackService {

}
