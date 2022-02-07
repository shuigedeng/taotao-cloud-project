import Taro from "@tarojs/taro";
import {Button, Input, Text, View} from "@tarojs/components";
import "./index.less";
import {drawCard} from "./service";
import React, {useEffect, useState} from "react";

import * as moment from "moment";

moment.locale("zh-cn");

interface IState {
  data: any;
  value: any;
}

const Index: Taro.FC = () => {
  let [state, setState] = useState<IState>({
    data: [],
    value: ''
  })

  useEffect(() => {

  }, [])

  const clickReceive = async () => {
    const {value} = state;
    var reg = /^[0-9]+.?[0-9]*$/;
    if (reg.test(value)) {
      try {
        await drawCard(value);
        Taro.showToast({
          title: "领取成功",
          icon: "none"
        });
      } catch (error) {
        Taro.showToast({
          title: error.graphQLErrors[0].message,
          icon: "none"
        });
      }
    } else {
      Taro.showToast({
        title: "请输入正确的兑换码",
        icon: "none"
      });
    }
  }

  // 搜索框内容修改
  const handleOnChange = (e) => {
    setState(prevState => {
      return {...prevState, value: e.detail.value}
    })
  }

  return (
    <View>
      <View className='code_view'>
        <Text>兑换码</Text>
        <Input
          placeholder='请输入兑换码'
          value={state.value}
          onInput={handleOnChange}
        />
      </View>
      <Text className='code_tips'>请输入六位数字，领取优惠券</Text>
      <Button
        className='code_btn'
        onClick={clickReceive}
      >提交</Button>
    </View>
  );
}

export default Index

