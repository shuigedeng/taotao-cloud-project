package com.taotao.cloud.im.biz.platform.modules.chat.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.platform.common.constant.HeadConstant;
import com.platform.common.shiro.ShiroUtils;
import com.platform.common.utils.ServletUtils;
import com.platform.common.web.service.impl.BaseServiceImpl;
import com.platform.modules.chat.dao.ChatFeedbackDao;
import com.platform.modules.chat.domain.ChatFeedback;
import com.platform.modules.chat.service.ChatFeedbackService;
import com.platform.modules.chat.vo.MyVo04;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 建议反馈 服务层实现
 * q3z3
 * </p>
 */
@Service("chatFeedbackService")
public class ChatFeedbackServiceImpl extends BaseServiceImpl<ChatFeedback> implements ChatFeedbackService {

    @Resource
    private ChatFeedbackDao chatFeedbackDao;

    @Autowired
    public void setBaseDao() {
        super.setBaseDao(chatFeedbackDao);
    }

    @Override
    public List<ChatFeedback> queryList(ChatFeedback t) {
        List<ChatFeedback> dataList = chatFeedbackDao.queryList(t);
        return dataList;
    }

    @Override
    public void addFeedback(MyVo04 myVo) {
        String version = ServletUtils.getRequest().getHeader(HeadConstant.VERSION);
        ChatFeedback feedback = BeanUtil.toBean(myVo, ChatFeedback.class)
                .setUserId(ShiroUtils.getUserId())
                .setVersion(version)
                .setCreateTime(DateUtil.date());
        this.add(feedback);
    }
}
