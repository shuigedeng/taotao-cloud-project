import React, {useEffect} from 'react'
import {Text, View} from '@tarojs/components'
import Taro from "@tarojs/taro";
import {AtButton} from 'taro-ui'
import './index.less'

const Index: Taro.FC = (props) => {
  useEffect(() => {

  }, []);

  const goLogin = () => {
    Taro.navigateTo({url: '/pages/login/index'})
  }

  return (
    <View className='index'>
      <Text>你好</Text>
      <AtButton type='primary'>I need Taro UI</AtButton>
      <Text>我的 我的</Text>
      <AtButton type='primary' circle={true} onClick={goLogin}>去登录</AtButton>
      <Text>我的？</Text>
      <AtButton type='secondary' circle={true}>我的</AtButton>
    </View>
  )
};

export default Index

