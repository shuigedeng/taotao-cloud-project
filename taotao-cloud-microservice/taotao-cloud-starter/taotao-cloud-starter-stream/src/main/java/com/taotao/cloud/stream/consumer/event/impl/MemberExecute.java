//package com.taotao.cloud.stream.consumer.event.impl;
//
//import cn.lili.event.MemberLoginEvent;
//import cn.lili.modules.member.entity.dos.Member;
//import cn.lili.modules.member.service.MemberService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
///**
// * 会员自身业务
// */
//@Service
//public class MemberExecute implements MemberLoginEvent {
//    @Autowired
//    private MemberService memberService;
//
//    @Override
//    public void memberLogin(Member member) {
//        memberService.updateMemberLoginTime(member.getId());
//    }
//}
