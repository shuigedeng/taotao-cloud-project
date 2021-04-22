import Taro, { Component, Config } from "@tarojs/taro";
import { View, Image, Button, Text, OpenData } from "@tarojs/components";
import "./index.less";
import { userInfo } from "../orderDetails/service";
import { couponsList, receive } from "../gifts/service";
import huiYuan from "../../img/qrCode.png";
import moment from "moment";


interface IState {
  current: number;
  query: {
    data?: any;
  };
  list?: any;
  data?: any;
}
export default class Home extends Component<null, IState> {
  config: Config = {
    navigationBarTitleText: "会员中心",
    enablePullDownRefresh: true,
    navigationBarBackgroundColor: "#e3034d",
    navigationBarTextStyle: "white",
  };

  state = {
    current: 0,
    query: {
      loading: true,
      data: {
        userInfo: {
          recordBalance: 1,
          balance: 1,
          fans: 0,
        }
      }
    },
    list: [],
    data: [],
  };

  async componentWillMount() {
    const userResult = await userInfo();
    const result = await couponsList('special');
    const { issueCoupon } = result.data;
    this.setState({
      query: userResult,
      data: issueCoupon
    });
  }

  async onPullDownRefresh() {
    const userResult = await userInfo();
    this.setState({
      query: userResult
    });
    setTimeout(() => {
      Taro.stopPullDownRefresh(); //停止下拉刷新
    }, 1000);
  }

  theInvitation() {
    Taro.showToast({
      title: "暂未开启！",
      icon: "none"
    });
  }


  handle(id) {
    switch (id) {
      case 1:
        Taro.navigateTo({
          url: "../reward/index"
        });
        break;
      default:
        break;
    }
  }

  // 跳转 余额 收益 粉丝 收益订单 绑定收款
  jumpMsg(id) {
    switch (id) {
      case 1:
        Taro.navigateTo({
          url: "../account/index"
        });
        break;
      case 2:
        // Taro.navigateTo({
        //   url: "../account/index"
        // });
        break;
      case 3:
        Taro.navigateTo({
          url: "../myFans/index"
        });
        break;
      case 4:
        Taro.navigateTo({
          url: "../fans/index"
        });
        break;
      case 5:
        Taro.showToast({
          title: "暂不提供，如需提现请联系客服",
          icon: "none"
        });
        // Taro.navigateTo({
        //   url: "../addCollection/index"
        // });
        break;
    
      default:
        break;
    }
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
    const { query, data } = this.state;
    const myMsg =[
      {
        id: 1,
        names: `${query.data.userInfo.balance /100}`,
        name: "余额"
      },
      {
        id: 2,
        names: `${query.data.userInfo.recordBalance /100}`,
        name: "累计收益"
      },
      {
        id: 3,
        names: `${query.data.userInfo.fans}`,
        name: "粉丝"
      },
      {
        id: 4,
        names: 0,
        name: "今日收益订单"
      },
      {
        id: 5,
        names: 0,
        icon: 'https://mengmao-qingying-files.oss-cn-hangzhou.aliyuncs.com/wallet.png',
        name: "绑定收款"
      }
    ];
    const convenient = [
      {
        id: 1,
        icon: huiYuan,
        name: "邀请二维码"
      },
      {
        id: 2,
        icon: 'https://mengmao-qingying-files.oss-cn-hangzhou.aliyuncs.com/benefits_3.png',
        name: "专享客服"
      }
    ];
    return (
      <View className="index">
        <View className="top-up1">
          <View className="top-up">
            <View className="userInformation">
              <View className="head">
                <OpenData type="userAvatarUrl" />
              </View>
              <View className="nameAndPhone">
                <OpenData type="userNickName" />
                <Text>会员时间：{query.data.userInfo.memberExpiredDate}</Text>
              </View>
            </View>

          </View>

        </View>

        <View className="ortherInformationBox at-row">
          {myMsg.map(order => (
            <View
              className="otherInformation at-col"
              key={order.id}
              onClick={this.jumpMsg.bind(this, order.id)}
            >
              {
                order.icon?(
                  <Image src={order.icon} className="icon" />
                ):(

                <Text className="orderName">{order.names}</Text>
                )
              }
              <Text className="orderName1">{order.name}</Text>
            </View>
          ))}
        </View>

        <View className="myStoreBox1">
          <View className="myOrdersTopLine">
            <Text className='coupons_num'></Text>
            <Text className="myOrderText">会员工具</Text>
          </View>
          <View className="ortherInformationBox1">
            {convenient.map(item => (
              <View key={item.id} className='oneConvenient'
                onClick={this.handle.bind(this, item.id)}>
                {
                  item.id !== 2 ? (
                    <Image src={item.icon} className="icon" />
                  ):(
                    <Button open-type="contact" className='icon_btn'>
                      <Image src={item.icon} className="icon" />
                    </Button>
                  )
                }
                <Text className='oneToolName'>
                  {item.name}
                </Text>
              </View>
            ))}
          </View>
        </View>

        <View className="myStoreBox1">
          <View className='coupons_title'>
            <Text className='coupons_num'></Text>
            <Text className='coupons_text'>专享优惠券</Text>
          </View>
          {/* 优惠券 */}
          {
            data.map(item=>(
              <View className="oneCoupons">
                <View className="oneCoupons_left">
                  ￥<Text className="price">{item.amount/100}</Text>
                </View>
                <View className='oneCoupons_semicircle'>
                  <View className='oneCoupons_top' />
                  <View className='oneCoupons_bottom' />
                </View>
                <View className='oneCoupons_right'>
                  <View className='oneCoupons_right_top'>
                    <Text className='oneCoupons_title1'>
                      满{item.require / 100}减{item.amount / 100}优惠券
                    </Text>
                    <Text className='oneCoupons_title2'>有效期至{moment(item.expiredDate).format('YYYY-MM-DD')}</Text>
                    <Text className='oneCoupons_title3' onClick={this.clickReceive.bind(this, item.id)}>点击领取</Text>
                  </View>
                </View>
              </View>
            ))
          }
        </View>
      </View>
    );
  }
}
