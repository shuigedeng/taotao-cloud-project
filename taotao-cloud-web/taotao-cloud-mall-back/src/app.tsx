import React, { useEffect } from "react";
import Taro, { getCurrentInstance } from "@tarojs/taro";
import { Provider } from "react-redux";

import configStore from "./store";

import sa from "./http/sa/miniSa";

import { Global } from "../global";

declare let global: Global;

const store = configStore();

const App: Taro.FC = (props) => {
  useEffect(() => {
    let currentInstance = getCurrentInstance();
    console.log(currentInstance);
    // if (options.query.mode === 'save') {
    //   Taro.setStorageSync('screenShot', true)
    //   Taro.setStorageSync('shop_id', options.query.shop_id)
    // }
    // const launchOptionsApp = Taro.getLaunchOptionsSync();
    // console.log(launchOptionsApp)

    Taro.getSystemInfo({
      success: (res) => {
        global = Object.assign(global, res, { debug: true });
        if (res.model && res.model.includes("iPhone X")) {
          global.iphoneX = true;
          console.log("是iphoneX机型");
        } else if (
          (res.platform === "ios" &&
            res.screenHeight === 812 &&
            res.screenWidth === 375) ||
          (res.screenHeight === 896 && res.screenWidth === 414)
        ) {
          global.iphoneX = true;
          console.log("是iphoneX机型");
        } else {
          global.iphoneX = false;
          console.log("不是iphoneX机型");
        }
        console.log("设备信息", global);

        let { safeArea, ...obj } = res;
        sa.registerApp(safeArea);

        // @ts-ignore
        let { host, ...data } = obj;

        sa.registerApp(data);
        if (host) {
          sa.registerApp(host);
        }
      },
    });

    Taro.getLocation({
      type: "wgs84",
      success: function (res) {
        sa.registerApp(res);
      },
    });

    sa.init();
  }, []);

  return <Provider store={store}>{props.children}</Provider>;
};

export default App;
