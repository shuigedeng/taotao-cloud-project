//package com.taotao.cloud.order.biz.rocketmq;
//
//@RocketMQTransactionListener(
// txProducerGroup = "msgRoducerGroup",
// corePoolSize = 2,
// maximumPoolSize = 5
//)
//public class MsgMQLocalTransactionListenerImpl implements RocketMQLocalTransactionListener {
//
//    // 发送half消息成功之后，mq返回成功，回调执行本地事务操作，并返回执行事务的结果给MQ,
//    @Override
//    public RocketMQLocalTransactionState executeLocalTransaction(Message msg, Object arg) {
//        String type = msg.getHeaders().get("status").toString();
//        System.out.println("executeLocalTransaction: msg-"+ msg + "-arg:" + arg +"-status:"+type);
//        switch (type) {
//            case "1":
//                System.out.println("事务执行状态未知");
//                return RocketMQLocalTransactionState.UNKNOWN;
//            case "2":
//                System.out.println("事务执行状态成功");
//                return RocketMQLocalTransactionState.COMMIT;
//            case "3":
//                System.out.println("事务执行状态失败");
//                return RocketMQLocalTransactionState.ROLLBACK;
//
//        }
//        return RocketMQLocalTransactionState.ROLLBACK ;
//    }
//
//    //当Mq 没有收到我们返回的事务状态信息 或者 返回的事务状态为RocketMQLocalTransactionState.UNKNOWN，会
//    // 再次发送消息过来确定消息的状态
//    @Override
//    public RocketMQLocalTransactionState checkLocalTransaction(Message msg) {
//        System.out.println("checkLocalTransaction:"+msg);
//        return RocketMQLocalTransactionState.COMMIT;
//    }
//}
