import Taro, { Component } from "@tarojs/taro";
import { View, Image, Text } from "@tarojs/components";
import "./index.less";

export default class paySuccess extends Component {
  config = {
    navigationBarTitleText: "支付成功"
  };

  componentWillUnmount() {}

  componentDidShow() {}

  componentDidHide() {}

  toHome() {
    Taro.switchTab({
      url: "../home/index"
    });
  }
  toOrder() {
    Taro.switchTab({
      url: "../mine/index"
    });
  }
  render() {
    return (
      <View className="index">
        <Image src='https://mengmao-qingying-files.oss-cn-hangzhou.aliyuncs.com/paySuccessIcon.png' className="payIcon" />
        <Text className="payText">订单支付成功</Text>
        <View className="bottomView">
          <View className="to" onClick={this.toOrder}>
            返回我的
          </View>
          <View className="to" onClick={this.toHome}>
            返回首页
          </View>
        </View>
      </View>
    );
  }
}

