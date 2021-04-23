import Taro from '@tarojs/taro';
import {Button, View} from '@tarojs/components';
import {showErrorToast} from '@/utils/util';


import './index.less';
import React, {useEffect, useState} from 'react';
import {setGlobalData} from "@/utils/global";
import {checkLogin, loginByWeixin} from "@/utils/user";

const Index: Taro.FC = () => {

  const [state, setState] = useState({})

  useEffect(() => {
    Taro.setNavigationBarTitle({title: "登录"})
    Taro.setNavigationBarColor({
      frontColor: "#ffffff",
      backgroundColor: "#6e0e0e"
    })
    Taro.setBackgroundColor({
      backgroundColor: '#ffffff',
      backgroundColorTop: '#ffffff', // 顶部窗口的背景色为白色
      backgroundColorBottom: '#ffffff', // 底部窗口的背景色为白色
    })
  })

  const accountLogin = () => {
    Taro.navigateTo({
      url: "/pages/auth/accountLogin/accountLogin"
    });
  }

  const wxLogin = (e) => {
    console.log(e.detail.userInfo)
    if (e.detail.userInfo == undefined) {
      setGlobalData('hasLogin', false)
      showErrorToast('微信登录失败');
      return;
    }

    checkLogin().catch(() => {
      loginByWeixin(e.detail.userInfo).then(() => {
        // setGlobalData('hasLogin', true)
        // Taro.navigateBack({
        //   delta: 1
        // })
      }).catch(() => {
        setGlobalData('hasLogin', false)
        showErrorToast('微信登录失败');
      });
    });
  }

  const getUserProfile = (e) => {
    console.log(e)

    Taro.getUserProfile({desc: '获取用户信息'}).then(res => {
      console.log(res)

      setGlobalData('hasLogin', true)
      Taro.navigateBack({
        delta: 1
      })
    }).catch(err => {

    })
  }

  const getPhoneNumber = (e) => {
    console.log(e)
  }

  return (
    <View className='container'>
      <View className='login-box'>
        <Button type='primary'
                className='wx-login-btn'
                onClick={getUserProfile}
                openType='getPhoneNumber'
                onGetPhoneNumber={getPhoneNumber}>微信授权登录</Button>

        <Button type='primary' className='account-login-btn'
                onClick={accountLogin}>账号登录</Button>
      </View>
    </View>
  );
}

export default Index
