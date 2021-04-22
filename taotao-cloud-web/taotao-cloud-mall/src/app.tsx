import React, {useEffect} from 'react'
import Taro, {useDidShow,} from "@tarojs/taro";
import {Provider} from "react-redux";
import {set as setGlobalData} from './pages/ucenter/global_data';
import configStore from "./store";
import {Global} from "../global";

// import 'windi.css';
import 'taro-ui/dist/style/index.scss'
import {logError} from "@/utils/error";
import {checkLogin} from "@/utils/user";

declare let global: Global;

const store = configStore();

const App: Taro.FC = (props) => {

  useDidShow(() => {
    // Taro.getSystemInfo({
    //   success: (res) => {
    //     global = Object.assign(global, res, {debug: true});
    //     if (res.model && res.model.includes("iPhone X")) {
    //       global.iphoneX = true;
    //       logError("是iphoneX机型");
    //     } else global.iphoneX = (res.platform === "ios" &&
    //       res.screenHeight === 812 &&
    //       res.screenWidth === 375) ||
    //       (res.screenHeight === 896 && res.screenWidth === 414);
    //     console.log("设备信息", global);
    //
    //     // let {safeArea, ...obj} = res;
    //     // sa.registerApp(safeArea);
    //     //
    //     // // @ts-ignore
    //     // let {host, ...data} = obj;
    //     //
    //     // sa.registerApp(data);
    //     // if (host) {
    //     //   sa.registerApp(host);
    //     // }
    //
    //     // sensors.track("submitLogin", {
    //     //   ProductName: "submitLogin",
    //     //   ProductPrice: 123.45,
    //     //   IsAddedToFav: false,
    //     // });
    //     // sensors.init();
    //   },
    // });

    checkLogin().then(res => {
      setGlobalData('hasLogin', true);
    }).catch(() => {
      setGlobalData('hasLogin', false);
    });
  })

  useEffect(() => {
    const update = () => {
      if (process.env.TARO_ENV === 'weapp') {
        if (Taro.canIUse("getUpdateManager")) {
          const updateManager = Taro.getUpdateManager();
          updateManager.onCheckForUpdate(res => {
            if (res.hasUpdate) {
              updateManager.onUpdateReady(function () {
                Taro.showModal({
                  title: "更新提示",
                  content: "新版本已经准备好，是否重启应用？",
                  success: function (successRes) {
                    // res: {errMsg: "showModal: ok", cancel: false, confirm: true}
                    if (successRes.confirm) {
                      // 新的版本已经下载好，调用 applyUpdate 应用新版本并重启
                      updateManager.applyUpdate();
                    }
                  }
                });
              });
              updateManager.onUpdateFailed(function () {
                // 新的版本下载失败
                Taro.showModal({
                  title: "已经有新版本了哟~",
                  content: "新版本已经上线啦~，请您删除当前小程序，重新搜索打开哟~"
                });
              });
            }
          });
        }
      }
    }
    update()
  }, []);

  return <Provider store={store}>{props.children}</Provider>;
};

export default App
