import Taro from "@tarojs/taro";
import {Button, Text, View,} from "@tarojs/components";
import {topUpList, userTopUp} from "./service";
import "./index.less";

// 引入moment
import * as moment from "moment";
import React, {useEffect, useState} from "react";

moment.locale("zh-cn");

interface IState {
  btnState1: boolean
  btnState2: boolean
  btnState3: boolean
  btnState4: boolean
  onclickId: number
  Top_upWay: any;
}

const Index: Taro.FC = () => {
  let [state, setState] = useState<IState>({
    btnState1: true,
    btnState2: false,
    btnState3: false,
    btnState4: false,
    onclickId: 1,
    Top_upWay: null,
  })

  useEffect(() => {
    const init = async () => {
      const topUpWay = await topUpList();
      const topUpList1 = topUpWay.data.topUpList
      setState(prevState => {
        return {...prevState, Top_upWay: topUpList1}
      })
    }
    init()
  }, [])

  const changeWay = (id) => {
    setState(prevState => {
      return {...prevState, onclickId: id}
    })
  }

  const topupNow = async () => {
    const {onclickId} = state;
    const user_TopUp = await userTopUp(onclickId);
    const userTopUp1 = user_TopUp.data.userTopUp;
    const cancel = "requestPayment:fail cancel";
    try {
      await Taro.requestPayment(userTopUp1);
      Taro.navigateTo({
        url: "../results/index"
      });
    } catch (error) {
      if (error.errMsg === cancel) {
        Taro.showToast({
          title: "用户取消了支付",
          icon: "none",
          duration: 1000
        });
      } else {
        Taro.showToast({
          title: "支付失败",
          icon: "none",
          duration: 1000,
          mask: true
        });
      }
    }
  }

  const {Top_upWay} = state;
  return (
    <View>
      <View className="Grid">
        {Top_upWay.map(Top_upWayItem => (
          <View
            className="gird {{onclickId===Top_upWayItem.id?'active_gird':''}}"
            onClick={() => changeWay(Top_upWayItem.id)}
            key={Top_upWayItem.id}
          >
            <Text className="grid_name">充值{Top_upWayItem.price / 100}元</Text>
            <Text className="grid_text">赠送{Top_upWayItem.givePoint / 100}积分</Text>
          </View>
        ))}
      </View>
      <Button
        className='btn_top_up'
        onClick={topupNow}
      >立即充值</Button>
      <View className='txt_top_up'>
        <Text>1.余额仅支持有地道微信小程序和APP端使用</Text>
        <Text>2.点击支付，即表示您已同意《充值协议》</Text>
        <Text>充值协议 </Text>
        <Text>尊敬的用户，为了保障您的合法权益，请您在充值前，仔细、完整地阅读以下充值协议。当您点击“充值”按钮，即视为已阅读、理解本协议，并同意按本协议规定执行。</Text>
        <Text>1.充值方式</Text>
        <Text>您可通过微信小程序或者APP客户端在线充值或者在有地道线下门店和线上使用现金充值；充值方式限银联支付、微信支付、支付宝支付、现金支付。</Text>
        <Text>2.充值活动</Text>
        <Text>用户通过充值可获取赠余额，具体根据活动（实际请以充值配置为准）;充值金额到账后，用户可通过重新登录账号进行查看；（如遇特殊活动，以活动页面规则为准）。</Text>
        <Text>3.使用限制</Text>
        <Text>账户充值的余额仅可用于有地道小程序和APP提供的商品支付服务。</Text>
        <Text>4.充值后，账户余额不设置有效期，如有需要可联系客服申请退款。</Text>
        <Text>如您对本协议有任何疑问或建议，可随时致电我们的客服：166 5516 0366</Text>
        <Text>充值说明:</Text>
        <Text>1.充值后金额立即到账余额，余额可在有地道购买任何商品。</Text>
        <Text>2.单个用户可充值多次，每次充值均可获得对赠送。</Text>
        <Text>3.充值后赠送立即到账，充值金额可以提现，提现时将扣除赠送部分和银行渠道费率。</Text>
      </View>
    </View>
  )

}


export default Index
