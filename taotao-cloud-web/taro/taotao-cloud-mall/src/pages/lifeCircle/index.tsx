import React, {useEffect} from 'react'
import {Text, View} from '@tarojs/components'

import './index.less'
import {eventCenter, getCurrentInstance, useDidHide, useDidShow, useReady} from "@tarojs/taro";
import {AtButton} from 'taro-ui';

const Index: Taro.FC = () => {
  let currentInstance = getCurrentInstance();
  // @ts-ignore
  const onShowEventId = currentInstance.router.onShow;

  useReady(() => {
    console.log("useReady-----------")

    // @ts-ignore
    const onReadyEventId = currentInstance.router.onReady
    eventCenter.once(onReadyEventId, () => {
      console.log(`${onReadyEventId}++++++++++++`)
    })
  })

  useDidShow(() => {
    eventCenter.on(onShowEventId, onShow)
  })

  const onShow = () => {
    console.log("onShowEventId------------")
  }

  useDidHide(() => {
    console.log('useDidHide---------------')
    eventCenter.off(onShowEventId, onShow)
  })


  useEffect(() => {
  }, []);

  return (
    <View className='index'>
      <Text>Hello world!</Text>
      <AtButton type='primary'>I need Taro UI</AtButton>
      <Text>我的 我的</Text>
      <AtButton type='primary' circle={true}>我的</AtButton>
      <Text>我的？</Text>
      <AtButton type='secondary' circle={true}>我的</AtButton>
    </View>
  )
};

export default Index

