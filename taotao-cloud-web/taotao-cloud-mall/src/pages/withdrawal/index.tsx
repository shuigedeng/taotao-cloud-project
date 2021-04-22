import Taro, { Component, Config } from "@tarojs/taro";
import { View, Image, Text, Button, OpenData } from "@tarojs/components";
import { toApplyForCashWithdrawals} from "./service";
import "./index.less";
import { userInfo } from "../orderDetails/service";

interface IState {
  query: {
    data?: any;
  };
  accountQuery: {
    data?: any;
  };
}
export default class certificates extends Component<any, IState>  {
  config: Config = {
    navigationBarTitleText: "余额提现"
  };
  state = {
    query: {
      loading: true,
      data: {
        userInfo: {
          balance: 1,
        }
      }
    },
    accountQuery: {
      loading: true,
      data: null
    },
  };


  async componentWillMount() {
    const userResult = await userInfo();
    this.setState({
      query: userResult
    });
  }
  async submit () {
    const name = Taro.getStorageSync("name");
    const account = Taro.getStorageSync("account");
    if (name === '' || account === '') {
      Taro.showToast({
        title: "请绑定账号",
        icon: "none"
      });
    }else {
    const { query } = this.state;
    const price = query.data.userInfo.balance / 100
    const collectionResult = await toApplyForCashWithdrawals();
    const response = collectionResult.data.toApplyForCashWithdrawals;
     if (price >= 1) { 
      if (response) {
        Taro.navigateTo({
          url: "../withdrawalSuccess/index"
        });
      }
     } else {
      Taro.showToast({
        title: "余额不足,无法提现！",
        icon: "none",
        duration: 1000,
        mask: true
      });
    }
    }
  };
  //跳转
  addCollection() {
    Taro.navigateTo({
      url: "../addCollection/index"
    });
  }

  render() {
    const { query } = this.state;
    const name = Taro.getStorageSync("name");
    const account = Taro.getStorageSync("account");
    return (
      <View className="index">
        <View className="myOrdersTopLine" onClick={this.addCollection}>
          <View className="myOrderText">
            <OpenData type="userAvatarUrl" className="img" />
            <View>
              <Text className="myOrderText1">支付宝真实姓名: {name}</Text>
              <Text className="myOrderText1">账户: {account}</Text>
            </View>
          </View>
          <View>
          </View>
        </View>
        <View className="tiQu">
          当前可提取余额<Text className="tiQu1"> {query.data.userInfo.balance/100} </Text>元
          </View>
        <Button className="button" onClick={this.submit}>
          确认提现
          </Button>
        <View className="tiQu">
          <Text className="myOrderText1">温馨提示:</Text>
          <Text className="myOrderText1">1、提现最迟24小时到账，节假日顺延</Text>
          <Text className="myOrderText1">2、请注意支付宝账号和姓名一致，否则会提现失败被退回</Text>
        </View>
      </View>
    );
  }
}
