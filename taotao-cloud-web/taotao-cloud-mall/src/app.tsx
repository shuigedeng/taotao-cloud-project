import React, {useEffect} from 'react'
import Taro, {useDidShow,} from "@tarojs/taro";
import {Provider} from "react-redux";
import {set as setGlobalData} from './pages/ucenter/global_data';
import configStore from "./store";
import {Global} from "../global";

import 'windi.css';
import 'taro-ui/dist/style/index.scss'
import {logError} from "@/utils/error";
import {checkLogin} from "@/utils/user";

declare let global: Global;

const store = configStore();

const App: Taro.FC = (props) => {

  useDidShow(() => {
    Taro.getSystemInfo({
      success: (res) => {
        global = Object.assign(global, res, {debug: true});
        if (res.model && res.model.includes("iPhone X")) {
          global.iphoneX = true;
          logError("是iphoneX机型");
        } else global.iphoneX = (res.platform === "ios" &&
          res.screenHeight === 812 &&
          res.screenWidth === 375) ||
          (res.screenHeight === 896 && res.screenWidth === 414);
        console.log("设备信息", global);

        // let {safeArea, ...obj} = res;
        // sa.registerApp(safeArea);
        //
        // // @ts-ignore
        // let {host, ...data} = obj;
        //
        // sa.registerApp(data);
        // if (host) {
        //   sa.registerApp(host);
        // }

        // sensors.track("submitLogin", {
        //   ProductName: "submitLogin",
        //   ProductPrice: 123.45,
        //   IsAddedToFav: false,
        // });
        // sensors.init();
      },
    });

    checkLogin().then(res => {
      setGlobalData('hasLogin', true);
    }).catch(() => {
      setGlobalData('hasLogin', false);
    });
  })

  useEffect(() => {
    const update = () => {
      if (process.env.TARO_ENV === 'weapp') {
        const updateManager = Taro.getUpdateManager();
        Taro.getUpdateManager().onUpdateReady(function () {
          Taro.showModal({
            title: '更新提示',
            content: '新版本已经准备好，是否重启应用？',
            success: function (res) {
              if (res.confirm) {
                // 新的版本已经下载好，调用 applyUpdate 应用新版本并重启
                updateManager.applyUpdate()
              }
            }
          })
        })
      }
    }
    update()

    // const checkUserLogin = () => {
    //   Taro.checkSession({
    //     success() {
    //       return Taro.getStorage({key: 'session3rd'})
    //     },
    //     fail() {
    //       return Taro.login().then(response => {
    //         console.log(response)
    //         return Taro.request({
    //           url: '后端接口',
    //           data: {
    //             code: response.code
    //           }
    //         })
    //         .then(res => {
    //           if (res.statusCode === 200) {
    //             Taro.setStorage({
    //               key: 'session3rd',
    //               data: res.data.data.session3rd
    //             })
    //           } else if (res.statusCode === 500) {
    //             Taro.showToast({
    //               title: '发生错误，请重试！',
    //               icon: 'none'
    //             })
    //           }
    //         })
    //       })
    //       .catch(err => {
    //         console.log(err);
    //         Taro.showToast({
    //           title: '发生错误，请重试!',
    //           icon: 'none'
    //         })
    //       })
    //     }
    //   })
    // }
    // checkUserLogin()
    //
    // const getUserInfo = () => {
    //   Taro.getSetting().then(res => {
    //     if (res.authSetting["scope.userInfo"]) {
    //       return true;
    //     } else {
    //       Taro.authorize({
    //         scope: 'scope.userInfo',
    //         success(res) {
    //
    //         },
    //         fail() {
    //           // 用户点击不允许引导重新获取授权
    //
    //         }
    //       })
    //       // <AtModal isOpened>
    //       //   <AtModalHeader>授权登录</AtModalHeader>
    //       //   <AtModalContent>
    //       //     您还尚未授权，请授权后登录
    //       //   </AtModalContent>
    //       //   <AtModalAction>
    //       //     <Button onClick={this.handleCancel}>取消</Button>
    //       //     <Button
    //       //       open-type="getUserInfo"
    //       //       onGetUserInfo={this.getUserInfo}
    //       //     >确定</Button>
    //       //   </AtModalAction>
    //       // </AtModal>
    //     }
    //   })
    //   // .then(res => {
    //   //   return Taro.getUserInfo();
    //   // }).then(res => {
    //   //   Taro.setStorage({
    //   //     key: 'userInfo',
    //   //     data: res.userInfo
    //   //   })
    //   // }).catch(err => {
    //   //   console.log(err)
    //   // })
    // }
    // getUserInfo()

  }, []);

  return <Provider store={store}>{props.children}</Provider>;
};

export default App
