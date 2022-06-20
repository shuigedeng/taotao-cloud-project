import {Button, View} from '@tarojs/components';
import Taro from '@tarojs/taro';
import './index.less';
import {bind} from './service';
import React, {useEffect, useState} from "react";

interface IState {
  userCode: string;
}

const Index: Taro.FC = () => {
  let [state, setState] = useState<IState>({userCode: '',})

  useEffect(() => {
    const init = async () => {
      const res = await Taro.login();
      const code = res.code;
      setState({
        userCode: code,
      });
    }
    init()
  }, [])


  //获取用户手机号
  const getPhoneNumber = async (e) => {
    const {userCode} = state;
    let iv = e.detail.iv;
    let encryptedData = e.detail.encryptedData;
    let code = userCode;
    const result = await bind(code, encryptedData, iv);
    let bindPhoneByUser = result.data.bindPhoneByUser.number
    Taro.setStorage({
      key: "number",
      data: bindPhoneByUser,
    });
    if (result.data.bindPhoneByUser.number) {
      Taro.showToast({
        title: '绑定成功',
        icon: 'success',
        mask: true,
        duration: 1000
      });
      setTimeout(() => {
        Taro.navigateBack({
          delta: 1
        })
      }, 800);
    }
  }

  return (
    <View className="main">
      <View className="bindPhone">
        <View className="text1">绑定手机号，享受优质服务！</View>
        <View className="text2">
          为了给小伙伴们提供更好的服务，希望您可以绑定手机号！
        </View>
        <Button
          openType="getPhoneNumber"
          onGetPhoneNumber={getPhoneNumber}
          className="button"
        >
          一键绑定手机号
        </Button>
      </View>
    </View>
  );
}

export default Index
