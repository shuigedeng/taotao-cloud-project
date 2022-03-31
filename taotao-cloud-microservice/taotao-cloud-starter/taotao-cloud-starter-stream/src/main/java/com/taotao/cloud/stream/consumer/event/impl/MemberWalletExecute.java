//package com.taotao.cloud.stream.consumer.event.impl;
//
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
///**
// * 会员钱包创建
// *
// */
//@Service
//public class MemberWalletExecute implements MemberRegisterEvent {
//
//    @Autowired
//    private MemberWalletService memberWalletService;
//
//    @Override
//    public void memberRegister(Member member) {
//        // 有些情况下，会同时创建一个member_id的两条数据
////        memberWalletService.save(member.getId(),member.getUsername());
//    }
//}
