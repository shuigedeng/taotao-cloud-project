import Taro from "@tarojs/taro";
import {Image, Text, View} from "@tarojs/components";
import "./index.less";
import React, {useEffect, useState} from "react";

const Index: Taro.FC = () => {
  let [state, setState] = useState({})

  useEffect(() => {

  }, [])

  const toHome = () => {
    Taro.switchTab({
      url: "../home/index"
    });
  }
  const toOrder = () => {
    Taro.switchTab({
      url: "../mine/index"
    });
  }

  return (
    <View className="index">
      <Image src='https://mengmao-qingying-files.oss-cn-hangzhou.aliyuncs.com/paySuccessIcon.png'
             className="payIcon"/>
      <Text className="payText">提现成功</Text>
      <View className="bottomView">
        <View className="to" onClick={toOrder}>
          返回我的
        </View>
        <View className="to" onClick={toHome}>
          返回首页
        </View>
      </View>
    </View>
  );
}

export default Index

