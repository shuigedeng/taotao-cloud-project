import {useEffect} from 'react'
import {Text, View} from '@tarojs/components'
import {AtButton} from 'taro-ui'

import "taro-ui/dist/style/components/button.scss" // 按需引入
import './index.less'
import {
  eventCenter,
  getCurrentInstance,
  getSystemInfo,
  useDidHide,
  useDidShow,
  useReady
} from "@tarojs/taro";
import React from 'react'


const Index: Taro.FC = (props) => {
  let currentInstance = getCurrentInstance();
  const onShowEventId = currentInstance.router.onShow;

  useReady(() => {
    console.log("useReady-----------")

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
      {/*<Text>Hello world!</Text>*/}
      {/*<AtButton type='primary'>I need Taro UI</AtButton>*/}
      {/*<Text>我的    我的</Text>*/}
      {/*<AtButton type='primary' circle={true}>我的</AtButton>*/}
      {/*<Text>我的？</Text>*/}
      {/*<AtButton type='secondary' circle={true}>我的</AtButton>*/}
    </View>
  )
};

export default Index

