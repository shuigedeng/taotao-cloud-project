import Taro from '@tarojs/taro'
import {Button} from '@tarojs/components'

import './index.scss'
import React, {useState} from "react";

export default function LoginButton(props) {
  const [isLogin, setIsLogin] = useState(false)

  async function onGetAuthorize(res) {
    setIsLogin(true)
    try {
      let userInfo = await Taro.getOpenUserInfo()

      let user = JSON.parse(userInfo).response
      const {avatar, nickName} = user

      await props.setLoginInfo(avatar, nickName)
    } catch (err) {
      console.log('onGetAuthorize ERR: ', err)
    }

    setIsLogin(false)
  }

  return (
    <Button
      openType="getAuthorize"
      scope="userInfo"
      onGetAuthorize={onGetAuthorize}
      type="primary"
      className="login-button"
      loading={isLogin}
    >
      支付宝登录
    </Button>
  )
}
