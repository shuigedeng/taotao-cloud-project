import Taro from "@tarojs/taro";
import {Button, View} from "@tarojs/components";
import "./index.less";
import {addAccount} from "./service";
import {AtInput} from 'taro-ui'
import React, {useEffect, useState} from "react";

interface IState {
  name: string
  account: string
  phone: string
  card: string
}

const Index: Taro.FC = () => {
  let [state, setState] = useState<IState>({
    name: '',
    account: '',
    phone: '',
    card: '',
  })

  useEffect(() => {

  }, [])

  const handeleClick = async () => {
    const {name, account, phone, card} = state;
    const result = await addAccount(name, phone, account, card);
    if (result.data.addAccount) {
      Taro.showToast({
        title: "用户绑定成功",
        icon: "none",
        duration: 1000
      });
      Taro.setStorage({
        key: "name",
        data: name
      });
      Taro.setStorage({
        key: "account",
        data: account
      });
      Taro.navigateTo({
        url: "../withdrawal/index"
      });
    } else {
      Taro.showToast({
        title: "绑定失败",
        icon: "none",
        duration: 1000,
        mask: true
      });
    }
  }

  const handleChange = (key, value) => {
    const obj = {};
    obj[key.key] = value;
    setState(prevState => {
      return {...prevState, ...obj}
    });
  }

  return (
    <View className="index">
      <View className="ortherInformationBox">
        <AtInput
          name='name'
          title='收款人'
          type='text'
          placeholder='请输入收款人姓名'
          value={state.name}
          onChange={handleChange.bind(this, {key: 'name'})}
        />
        <AtInput
          name='account'
          title='账户'
          type='text'
          placeholder='请输入账户'
          value={state.account}
          onChange={handleChange.bind(this, {key: 'account'})}
        />
        <AtInput
          name='card'
          title='银行卡号'
          type='number'
          placeholder='请输入银行卡号'
          value={state.card}
          onChange={handleChange.bind(this, {key: 'card'})}
        />
        <AtInput
          name='phone'
          title='手机号'
          type='number'
          placeholder='请输入手机号'
          value={state.phone}
          onChange={handleChange.bind(this, {key: 'phone'})}
        />

        <Button className="orderName" onClick={handeleClick}>立即绑定</Button>

      </View>
    </View>
  );
}


export default Index
