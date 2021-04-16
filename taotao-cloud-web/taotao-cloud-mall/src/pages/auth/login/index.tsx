import Taro from '@tarojs/taro';
import {Button, View} from '@tarojs/components';
import {showErrorToast} from '@/utils/util';
import {set as setGlobalData} from '../../ucenter/global_data';

import * as user from '../../../utils/user';

import './index.less';
import React, {useState} from 'react';

const Index: Taro.FC = () => {

  const [state, setState] = useState({})

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

    user.checkLogin().catch(() => {
      user.loginByWeixin(e.detail.userInfo).then(() => {
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

  const  getUserProfile =(e) => {
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

  return (
    <View className='container'>
      <View className='login-box'>
        <Button type='primary' openType='getUserInfo' className='wx-login-btn'
                onGetUserInfo={wxLogin} onClick={getUserProfile}>微信授权登录</Button>
        <Button type='primary' className='account-login-btn'
                onClick={accountLogin}>账号登录</Button>
      </View>
    </View>
  );
}

export default Index
