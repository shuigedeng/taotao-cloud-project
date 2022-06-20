// import api from '@services/api'
// import app from '@services/request'
// import storage from '@utils/storage'
//
// const fetchSessionKey = (code: string) => {
//   return new Promise((resolve) => {
//     app.request({
//       url: app.apiUrl(api.getSessionKeyByCode),
//       data: {
//         code
//       }
//     }).then((result: any) => {
//       storage.setItem('session_key', result.session_key, 'login')
//       resolve(result)
//     })
//   })
// }
//
// interface IDecryptParam {
//   sessionKey: string
//   encryptedData: string
//   iv: string
// }
//
// const fetchDecryptData = (decryptParam: IDecryptParam) => {
//   return new Promise((resolve) => {
//     app.request({
//       method: 'POST',
//       url: app.apiUrl(api.decryptData),
//       data: decryptParam
//     }, { loading: false }).then((result: any) => {
//       resolve(result)
//     })
//   })
// }
//
// import React, { useState } from 'react'
// import Taro from '@tarojs/taro'
// import { View, Text, Button } from '@tarojs/components'
// import storage from '@utils/storage'
// import NavBar from '@components/navbar/index'
// import './index.scss'
//
// const Login = () => {
//   const navData = {
//     title: '',
//     back: true,
//     color: '#000000',
//     backgroundColor: '#ffffff'
//   }
//   const [loginCode, setLoginCode] = useState<string>('')
//
//   Taro.setNavigationBarColor({
//     frontColor: navData.color,
//     backgroundColor: navData.backgroundColor
//   })
//
//   const handleLogin = () => {
//     Taro.login({
//       success: function (res) {
//         if (res.code) {
//           setLoginCode(res.code)
//         }
//       }
//     })
//   }
//
//   const getUserInfo = (e) => {
//     const errMsg = e.detail.errMsg
//     if (errMsg === 'getUserInfo:ok') {
//       fetchSessionKey(loginCode).then((result: any) => {
//         fetchDecryptData({
//           sessionKey: result.session_key,
//           encryptedData: e.detail.encryptedData,
//           iv: e.detail.iv
//         }).then((result: any) => {
//           const user = {
//             nickName: result.nickName,
//             avatarUrl: result.avatarUrl,
//           }
//           storage.setItem('user', user, 'login')
//           Taro.navigateBack({
//             delta: 1
//           })
//         })
//       })
//     }
//   }
//
//   const handleLoginByPhone = () => {
//     Taro.navigateTo({
//       url: '/login/phone/index'
//     })
//   }
//
//   return (
//     <View className="login">
//       <NavBar {...navData} />
//       <View className="login-header">
//         <Text className="title">房产在线</Text>
//         <Text className="small">Fczx.com</Text>
//       </View>
//       <View className="login-content">
//         <View className="login-memo">
//           <View className="cut-line"></View>
//           <Text className="desc">推荐使用登录方式</Text>
//         </View>
//         <Button
//           className="btn btn-primary"
//           openType="getUserInfo"
//           onGetUserInfo={getUserInfo}
//           onClick={handleLogin}
//         >
//           <Text>微信登录</Text>
//         </Button>
//         <View className="btn btn-plain" onClick={handleLoginByPhone}>
//           <Text>手机快捷登录</Text>
//         </View>
//       </View>
//     </View>
//   )
// }
//
// export default Login
