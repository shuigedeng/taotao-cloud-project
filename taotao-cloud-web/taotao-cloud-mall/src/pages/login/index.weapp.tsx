import Taro from '@tarojs/taro'
import {Button, View} from '@tarojs/components'
import React, {Component} from 'react'
import './index.less'

class Login extends Component {
  //验证是否登录,进行自动登录的操作
  async componentDidMount() {
    let that = this;
    Taro.checkSession({
      success() {
        let code = Taro.getStorageSync('key');
        if (code) {
          that.info();
          Taro.switchTab({url: '/pages/index/index'});
        }
      },
      fail() {
        that.info();
      }
    })
  }

  //未登录时,快捷登录操作
  info = () => {
    Taro.getUserInfo({
      success: function (res) {
        Taro.login({
          success: async (res) => {
            console.log(res.code);
            //获取open_id和session_id
            let code = await loginCode(res.code);
            Taro.setStorageSync('key', code.data);
          }
        })
        Taro.switchTab({url: '/pages/index/index'});
      },
      fail(res) {
        Taro.showToast({
          icon: 'none',
          title: '请先授权登录'
        })
      }
    })
  }

  render() {
    return (
      <View className='l-c'>
        <Button type='primary' size='default' openType='getUserInfo'
                onGetUserInfo={this.info}>快捷登录</Button>
      </View>
    )
  }
}

export default Login
