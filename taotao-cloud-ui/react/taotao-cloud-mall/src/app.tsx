import React, {useEffect} from 'react'
import Taro, {useDidShow,} from "@tarojs/taro";
import {Provider} from "react-redux";
import configStore from "./store";

import './windi.css';
// import 'taro-ui/dist/style/index.scss'
import {checkLogin} from "@/utils/user";
import {setGlobalData} from "@/utils/global";
import * as weappSensors from './http/sa/miniSensors';

const store = configStore();

const App: Taro.FC = (props) => {
  useDidShow(() => {
    Taro.getSystemInfo({
      success: (res) => {
        let {safeArea, ...obj} = res;
        let assign = Object.assign({}, safeArea, obj);
        console.log(assign)
        weappSensors.default.registerApp(assign)
        weappSensors.default.init()
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
    update()
  }, []);

  return <Provider store={store}>{props.children}</Provider>;
};

export default App
