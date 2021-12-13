import Taro from "@tarojs/taro";

let RN: any = {};
if (IS_RN) {
  RN = require("react-native");
}

export const isAndriod = () => {
  if (IS_RN) {
    return RN.Platform.OS === "android";
  }
  const { platform, system } = Taro.getSystemInfoSync();
  return platform === "devtools"
    ? system.includes("Android")
    : platform === "Android";
};

export const initBackHandler = (callback: () => boolean = () => false) => {
  // callback 返回 true 阻止返回 默认返回false
  if (IS_RN) {
    RN.BackHandler.addEventListener("hardwareBackPress", function() {
      if (Taro.getCurrentPages().length === 1) {
        const result = callback();
        !result && Taro.navigateBack({ delta: 1 });
        return result;
      }
      return callback();
    });
  }
};
