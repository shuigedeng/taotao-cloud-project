import React, { useEffect } from "react";
import Taro, { getCurrentInstance } from "@tarojs/taro";
import { Provider } from "react-redux";

import configStore from "./store";

import sensors from "./http/sa/jsSa";
import { Global } from "../global";

declare let global: Global;

const store = configStore();

const App: Taro.FC = (props) => {
  useEffect(() => {
    let currentInstance = getCurrentInstance();
    console.log(currentInstance);

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

        sensors.track("submitLogin", {
          ProductName: "submitLogin",
          ProductPrice: 123.45,
          IsAddedToFav: false,
        });
      },
    });

    sensors.init();
  }, []);

  return <Provider store={store}>{props.children}</Provider>;
};

export default App;
