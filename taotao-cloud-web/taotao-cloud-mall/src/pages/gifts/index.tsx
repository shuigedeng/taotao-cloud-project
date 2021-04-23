import Taro from "@tarojs/taro";
import {Image, Text, View} from "@tarojs/components";
import "./index.less";
import {couponsList, receive} from "./service";
import React, {useEffect, useState} from "react";

var moment = require("moment");
moment.locale("zh-cn");

interface IState {
  data: any;
}


// receiveCoupons
const Index: Taro.FC = () => {
  let [state, setState] = useState<IState>({
    data: [],
  })

  useEffect(() => {
    const init = async () => {
      const result = await couponsList('ordinary');
      const {issueCoupon} = result.data;

      setState({
        data: issueCoupon
      });
    }
    init()
  }, [])
  const clickReceive = async (id) => {
    const receiveResult = await receive(id);
    if (receiveResult.data.drawCoupon.id !== null) {
      Taro.showToast({
        title: "领取成功",
        icon: "none"
      });
    } else {
      Taro.showToast({
        title: "请勿重复领取",
        icon: "none"
      });
    }
  }

  const {data} = state;
  return (
    <View>
      {data.map(item => (
        <View key={item.id} className="oneCoupons">
          {
            moment(moment(new Date()).format('YYYY-MM-DD')).diff(moment(item.expiredDate).format('YYYY-MM-DD'), 'day') >= 0 ?
              <View className='alreadyOverdue'>
                <Image
                  src='https://mengmao-qingying-files.oss-cn-hangzhou.aliyuncs.com/alreadyOverdue.png'/>
              </View>
              : null
          }
          <View className="oneCoupons-left">
            <Text className="price">￥{item.amount / 100}</Text>
          </View>
          <View className="oneCoupons-right">
            <View className="oneCoupons-right-top">
              <View className='oneCoupons-require'>
                <Text className="fullPrice">
                  满{item.require / 100}减{item.amount / 100}
                </Text>
                <Text
                  className="immediatelyText"
                  onClick={() => clickReceive(item.id)}
                >领取</Text>
              </View>
              <Text className="type">
                过期时间:
                {moment(item.expiredDate).format("YYYY-MM-DD")}
              </Text>
            </View>
          </View>
        </View>
      ))}
    </View>
  );
}


export default Index

