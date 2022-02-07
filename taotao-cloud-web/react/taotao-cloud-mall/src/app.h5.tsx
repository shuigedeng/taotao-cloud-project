import React from 'react'
import Taro, {useDidShow,} from "@tarojs/taro"
import {Provider} from "react-redux"
import configStore from "./store"

import 'windi.css'
import 'taro-ui/dist/style/index.scss'
import {checkLogin} from "@/utils/user"
import {setGlobalData} from "@/utils/global"
import * as h5Sensors from './http/sa/jsSsensors'

const store = configStore()

const App: Taro.FC = (props) => {
  useDidShow(() => {
    Taro.getSystemInfo({
      success: (res) => {
        h5Sensors.default.registerPage(res)
      },
    })

    checkLogin().then(res => {
      setGlobalData('hasLogin', true)
    }).catch(() => {
      setGlobalData('hasLogin', false)
    })
  })

  return <Provider store={store}>{props.children}</Provider>
}

export default App
