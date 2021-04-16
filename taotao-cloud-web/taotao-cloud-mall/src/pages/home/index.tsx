import React, {useEffect} from 'react'
import {Button, Text, View} from '@tarojs/components'
import {AtButton} from 'taro-ui'

import './index.less'


const Index: Taro.FC = (props) => {
  const tobegin = () => {
    Taro.redirectTo({
      url: '/pages/main/index'
    })
  };

  useEffect(() => {
    // try {
    //   const value = Taro.getStorageSync('userInfo')
    //   if (value) {
    //     Taro.redirectTo({
    //       url: '/pages/main/index'
    //     })
    //   }
    // } catch (e) {
    //   // Do something when catch error
    // }
  }, [])


  return (
    <View>
      <Button className='btn-special'>A Special Button</Button>
      <AtButton type='primary'>按钮文案</AtButton>
      <Text className='cyan-300 red-100 bg-red-200'>flsalflsafd</Text>
      <View className="red-100">hello</View>

      <View className="w-1_5">hello123</View>
    </View>
  )
};

export default Index

