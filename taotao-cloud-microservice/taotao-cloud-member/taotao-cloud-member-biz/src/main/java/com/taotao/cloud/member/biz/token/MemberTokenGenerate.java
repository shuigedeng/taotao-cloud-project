package com.taotao.cloud.member.biz.token;

import com.taotao.cloud.common.enums.ClientTypeEnum;
import com.taotao.cloud.common.enums.UserEnums;
import com.taotao.cloud.member.biz.connect.token.Token;
import com.taotao.cloud.member.biz.connect.token.base.AbstractTokenGenerate;
import com.taotao.cloud.member.biz.entity.Member;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 会员token生成
 */
@Component
public class MemberTokenGenerate extends AbstractTokenGenerate<Member> {
    @Autowired
    private TokenUtil tokenUtil;
    @Autowired
    private RocketmqCustomProperties rocketmqCustomProperties;

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Override
    public Token createToken(Member member, Boolean longTerm) {

        //获取客户端类型
        String clientType = ThreadContextHolder.getHttpRequest().getHeader("clientType");
        ClientTypeEnum clientTypeEnum;
        try {
            //如果客户端为空，则缺省值为PC，pc第三方登录时不会传递此参数
            if (clientType == null) {
                clientTypeEnum = ClientTypeEnum.PC;
            } else {
                clientTypeEnum = ClientTypeEnum.valueOf(clientType);
            }
        } catch (IllegalArgumentException e) {
            clientTypeEnum = ClientTypeEnum.UNKNOWN;
        }
        //记录最后登录时间，客户端类型
        member.setLastLoginDate(new Date());
        member.setClientEnum(clientTypeEnum.name());
        String destination = rocketmqCustomProperties.getMemberTopic() + ":" + MemberTagsEnum.MEMBER_LOGIN.name();
        rocketMQTemplate.asyncSend(destination, member, RocketmqSendCallbackBuilder.commonCallback());

        AuthUser authUser = new AuthUser(member.getUsername(), member.getId(), member.getNickName(), member.getFace(), UserEnums.MEMBER);
        //登陆成功生成token
        return tokenUtil.createToken(member.getUsername(), authUser, longTerm, UserEnums.MEMBER);
    }

    @Override
    public Token refreshToken(String refreshToken) {
        return tokenUtil.refreshToken(refreshToken, UserEnums.MEMBER);
    }

}
