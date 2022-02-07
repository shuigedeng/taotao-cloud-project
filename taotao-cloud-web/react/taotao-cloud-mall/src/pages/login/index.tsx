import Taro from '@tarojs/taro'
import {Button, View, Text} from '@tarojs/components'
import React, {Component, useEffect, useState} from 'react'

// class Login extends Component {
//   //验证是否登录,进行自动登录的操作
//   async componentDidMount() {
//     let that = this;
//     Taro.checkSession({
//       success() {
//         let code = Taro.getStorageSync('key');
//         if (code) {
//           that.info();
//           Taro.switchTab({url: '/pages/index/index'});
//         }
//       },
//       fail() {
//         that.info();
//       }
//     })
//   }
//
//   //未登录时,快捷登录操作
//   info = () => {
//     Taro.getUserInfo({
//       success: function (res) {
//         Taro.login({
//           success: async (res) => {
//             console.log(res.code);
//             //获取open_id和session_id
//             // let code = await loginCode(res.code);
//             // Taro.setStorageSync('key', code.data);
//           }
//         })
//         Taro.switchTab({url: '/pages/index/index'});
//       },
//       fail(res) {
//         Taro.showToast({
//           icon: 'none',
//           title: '请先授权登录'
//         })
//       }
//     })
//   }
//
//   render() {
//     return (
//       <View className='l-c'>
//         <Button type='primary' size='default' openType='getUserInfo'
//                 onGetUserInfo={this.info}>快捷登录</Button>
//       </View>
//     )
//   }
// }
//
// export default Login

const fetchSessionKey = (code: string) => {
  return new Promise((resolve) => {
    // app.request({
    //   url: app.apiUrl(api.getSessionKeyByCode),
    //   data: {
    //     code
    //   }
    // }).then((result: any) => {
    //   storage.setItem('session_key', result.session_key, 'login')
    //   resolve(result)
    // })
  })
}

interface IDecryptParam {
  sessionKey: string
  encryptedData: string
  iv: string
}

const fetchDecryptData = (decryptParam: IDecryptParam) => {
  return new Promise((resolve) => {
    // app.request({
    //   method: 'POST',
    //   url: app.apiUrl(api.decryptData),
    //   data: decryptParam
    // }, { loading: false }).then((result: any) => {
    //   resolve(result)
    // })
  })

}

const Login: Taro.FC = () => {
  const navData = {
    title: '',
    back: true,
    color: '#000000',
    backgroundColor: '#ffffff'
  }
  const [loginCode, setLoginCode] = useState<string>('')

  useEffect(() => {
    Taro.setNavigationBarColor({
      frontColor: navData.color,
      backgroundColor: navData.backgroundColor
    })

    Taro.getSetting().then(res => {
      console.log(res)
      if (res.authSetting["scope.userInfo"]) {
        return true;
      } else {
        Taro.authorize({
          scope: 'scope.userInfo',
          success(res) {
            console.log(res)

          },
          fail() {
            // 用户点击不允许引导重新获取授权

          }
        })
        // <AtModal isOpened>
        //   <AtModalHeader>授权登录</AtModalHeader>
        //   <AtModalContent>
        //     您还尚未授权，请授权后登录
        //   </AtModalContent>
        //   <AtModalAction>
        //     <Button onClick={this.handleCancel}>取消</Button>
        //     <Button
        //       open-type="getUserInfo"
        //       onGetUserInfo={this.getUserInfo}
        //     >确定</Button>
        //   </AtModalAction>
        // </AtModal>
      }
    })

  }, []);

  const handleLogin = () => {
    Taro.login({
      success: function (res) {
        console.log(res)
        if (res.code) {
          setLoginCode(res.code)
        }
      }
    })
  }

  const getUserInfo = (e) => {
    const errMsg = e.detail.errMsg
    if (errMsg === 'getUserInfo:ok') {
      fetchSessionKey(loginCode).then((result: any) => {
        fetchDecryptData({
          sessionKey: result.session_key,
          encryptedData: e.detail.encryptedData,
          iv: e.detail.iv
        }).then((result: any) => {
          const user = {
            nickName: result.nickName,
            avatarUrl: result.avatarUrl,
          }
          // storage.setItem('user', user, 'login')
          Taro.navigateBack({
            delta: 1
          })
        })
      })
    }
  }

  const handleLoginByPhone = () => {
    Taro.navigateTo({
      url: '/login/phone/index'
    })
  }

  return (
    <View className="login">
      <View className="login-header">
        <Text className="title">房产在线</Text>
        <Text className="small">Fczx.com</Text>
      </View>
      <View className="login-content">
        <View className="login-memo">
          <View className="cut-line"></View>
          <Text className="desc">推荐使用登录方式</Text>
        </View>
        <Button
          className="btn btn-primary"
          openType="getUserInfo"
          onGetUserInfo={getUserInfo}
          onClick={handleLogin}
        >
          <Text>微信登录</Text>
        </Button>
        <View className="btn btn-plain" onClick={handleLoginByPhone}>
          <Text>手机快捷登录</Text>
        </View>
      </View>


    </View>
  )
}

export default Login
