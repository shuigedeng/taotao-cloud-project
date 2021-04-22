import Taro, { Component } from "@tarojs/taro";
import { View, Text, Image } from "@tarojs/components";
import "./index.less";
import { couponsList, receive } from "./service";

var moment = require("moment");
moment.locale("zh-cn");

interface IState {
  data: any;
}
export default class receiveCoupons extends Component<null, IState> {
  config = {
    navigationBarTitleText: "领取优惠券"
  };
  state = {
    data: [],
  };

  async componentWillMount() {
    const result = await couponsList('ordinary');
    const { issueCoupon } = result.data;

    this.setState({
      data: issueCoupon
    });
  }

  async clickReceive(id) {
    const receiveResult = await receive(id);
    if (receiveResult.data.drawCoupon.id !== null) {
      Taro.showToast({
        title: "领取成功",
        icon: "none"
      });
    } else {
      Taro.showToast({
        title: "请勿重复领取",
        icon: "none"
      });
    }
  }

  render() {
    const { data } = this.state;
    return (
      <View>
        {data.map(item => (
          <View key={item.id} className="oneCoupons">
            {
              moment(moment(new Date()).format('YYYY-MM-DD')).diff(moment(item.expiredDate).format('YYYY-MM-DD'), 'day')>=0?
                <View className='alreadyOverdue'>
                  <Image src='https://mengmao-qingying-files.oss-cn-hangzhou.aliyuncs.com/alreadyOverdue.png' />
                </View>
              :null
            }
            <View className="oneCoupons-left">
              <Text className="price">￥{item.amount / 100}</Text>
            </View>
            <View className="oneCoupons-right">
              <View className="oneCoupons-right-top">
                <View className='oneCoupons-require'>
                  <Text className="fullPrice">
                    满{item.require / 100}减{item.amount / 100}
                  </Text>
                  <Text 
                    className="immediatelyText" 
                    onClick={this.clickReceive.bind(this, item.id)}
                  >领取</Text>
                </View>
                <Text className="type">
                  过期时间:
                  {moment(item.expiredDate).format("YYYY-MM-DD")}
                </Text>
              </View>
            </View>
          </View>
        ))}
      </View>
    );
  }
}

