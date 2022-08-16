import {Button} from '@tarojs/components'
import './index.weapp.less'
import React, {useState} from "react";

export default function Index(props) {
  const [isLogin, setIsLogin] = useState(false)

  async function onGetUserInfo(e) {
    setIsLogin(true)

    const {avatarUrl, nickName} = e.detail.userInfo
    await props.setLoginInfo(avatarUrl, nickName)

    setIsLogin(false)
  }

  return (
    <Button
      openType="getUserInfo"
      onGetUserInfo={onGetUserInfo}
      type="primary"
      className="login-button"
      loading={isLogin}
    >
      微信登录
    </Button>
  )
}
