/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.payment.biz.demo.test;

import com.yungouos.pay.entity.ProfitSharingInfo;
import com.yungouos.pay.finance.Finance;
import java.util.List;

/**
 * 资金工具演示demo
 *
 * @author YunGouOS技术部-029
 */
public class FinanceTest {

    public static void main(String[] args) {

        // 商户号
        String mch_id = "商户号";
        // 商户密钥
        String key = "支付密钥";

        /**
         * 划重点：分账接口，不是每个人都有的。如需开通请联系yungouos客服进行申请。
         *
         * <p>以下是分账功能演示 主要演示大概流程 分为四个步骤
         *
         * <p>配置分账->生成分账账单（前提有已支付的订单）->发起分账付款（针对分账账单）->查询分账结果
         *
         * <p>名词解释：
         *
         * <p>配置分账： 主要作用是
         * 配置一些分账收款人的账户信息还有分账比例等，不一定每次都需要调用，配置一次就好。该接口不一定要自己对接，可以在yungouos官网进行配置，自行记录分账配置单号就可以
         *
         * <p>生成分账账单： 首先明白一个前提，分账是针对订单的，也就意味着必须要有一个已支付的订单号。有了订单号那么我们需要知道这个订单给谁分多少钱？
         * 即该接口的作用，针对订单和配置单号，系统计算出相关金额以及分账接收方数据，同一笔订单支持多个收款人分账，也就是多个配置单号，使用,号分割。
         *
         * <p>分账支付：有了分账账单了，账单已经计算好分多少钱，分给谁那么下面干什么？当然是进行付款的动作了，即调用该接口告诉微信，我要进行分账付钱了。
         *
         * <p>查询分账结果： 已经告诉微信要去分账付钱了，那付成没成功即该接口作用，查询分账账单的付款结果。
         *
         * <p>通过上述解释，想必大家已经对分账有了大致了解，配置分账主要是设置收款人账户以及分账比例等信息，这样每笔订单分账的时候咱们不需要每次传递那么多收款人信息
         *
         * <p>只需要传递配置单号即可。其次可能为什么需要生成分账账单，单个订单可能给N个人进行分账，那么此时如果不先生成账单而采用一次性的操作，其中涉及到微信接口的调用
         *
         * <p>多账单进行发起支付结果不可控，那么事务方面必然不好处理。故我们将其拆分，先生成账单，后分别对账单进行分账支付，这样让各个分账收款方不受“事务”的干扰
         */

        /** 分账配置，返回配置单号。后续生成分账账单需要用 */
        String configNo = Finance.wxPayConfig(
                mch_id, null, "测试分账", "o-_-itxeWVTRnl-iGT_JJ-t3kpxU", null, null, "0.12", null, key);
        LogUtils.info("微信分账配置结果：" + configNo);

        /** 支付宝分账配置 */
        String aliPayConfigNo = Finance.aliPayConfig("支付宝商户号", "测试分账", "分账收款方支付宝账户", "分账收款方支付宝姓名", null, null, "支付宝密钥");
        LogUtils.info("微信分账配置结果：" + aliPayConfigNo);

        /** 生成分账账单 */
        List<String> list = Finance.createBill(mch_id, "1582434286538", configNo, null, null, null, key);
        LogUtils.info("生成分账账单结果：" + list.toString());

        // 分账账单号 此处不要学哦 没有做数据合法性校验 只是大概演示个流程给大家瞅瞅
        String psNo = list.get(0);

        /** 发起分账支付 */
        boolean flag = Finance.sendPay(mch_id, psNo, "测试分账", key);
        LogUtils.info("发起分账支付结果：" + flag);

        /** 查询分账结果 */
        ProfitSharingInfo profitSharingInfo = Finance.getInfo(mch_id, psNo, key);
        LogUtils.info("查询分账结果：" + profitSharingInfo.toString());

        /** 完结分账 */
        boolean flagFinish = Finance.finish(mch_id, "WD1582768731405958219", key);
        LogUtils.info("完结分账结果：" + flagFinish);
    }
}
