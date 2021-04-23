import Taro from "@tarojs/taro";
import {Button, Image, OpenData, Text, View} from "@tarojs/components";
import "./index.less";
import {userInfo} from "../orderDetails/service";
import {couponsList, receive} from "../gifts/service";
import huiYuan from "../../img/qrCode.png";
import moment from "moment";
import React, {useEffect, useState} from "react";
import usePullDownRefresh = Taro.usePullDownRefresh;


interface IState {
  current: number;
  query: {
    data?: any;
    loading: boolean
  };
  list?: any;
  data?: any;
}


const Index: Taro.FC = () => {
  let [state, setState] = useState<IState>({
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
  })

  useEffect(() => {
    const init = async () => {
      const userResult = await userInfo();
      const result = await couponsList('special');
      const {issueCoupon} = result.data;
      setState(prevState => {
        return {
          ...prevState, query: userResult, data: issueCoupon
        }
      })
    }
    init()
  }, [])

  usePullDownRefresh(async () => {
    const userResult = await userInfo();
    setState(prevState => {
      return {...prevState, query: userResult}
    })
    setTimeout(() => {
      Taro.stopPullDownRefresh(); //停止下拉刷新
    }, 1000);
  })

  const theInvitation = () => {
    Taro.showToast({
      title: "暂未开启！",
      icon: "none"
    });
  }

  const handle = (id) => {
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
  const jumpMsg = (id) => {
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

  const clickReceive = async (id) => {
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

  const {query, data} = state;
  const myMsg = [
    {
      id: 1,
      names: `${query.data.userInfo.balance / 100}`,
      name: "余额"
    },
    {
      id: 2,
      names: `${query.data.userInfo.recordBalance / 100}`,
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
              <OpenData type="userAvatarUrl"/>
            </View>
            <View className="nameAndPhone">
              <OpenData type="userNickName"/>
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
            onClick={jumpMsg.bind(this, order.id)}
          >
            {
              order.icon ? (
                <Image src={order.icon} className="icon"/>
              ) : (

                <Text className="orderName">{order.names}</Text>
              )
            }
            <Text className="orderName1">{order.name}</Text>
          </View>
        ))}
      </View>

      <View className="myStoreBox1">
        <View className="myOrdersTopLine">
          <Text className='coupons_num'/>
          <Text className="myOrderText">会员工具</Text>
        </View>
        <View className="ortherInformationBox1">
          {convenient.map(item => (
            <View key={item.id} className='oneConvenient'
                  onClick={handle.bind(this, item.id)}>
              {
                item.id !== 2 ? (
                  <Image src={item.icon} className="icon"/>
                ) : (
                  <Button open-type="contact" className='icon_btn'>
                    <Image src={item.icon} className="icon"/>
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
          data.map(item => (
            <View className="oneCoupons">
              <View className="oneCoupons_left">
                ￥<Text className="price">{item.amount / 100}</Text>
              </View>
              <View className='oneCoupons_semicircle'>
                <View className='oneCoupons_top'/>
                <View className='oneCoupons_bottom'/>
              </View>
              <View className='oneCoupons_right'>
                <View className='oneCoupons_right_top'>
                  <Text className='oneCoupons_title1'>
                    满{item.require / 100}减{item.amount / 100}优惠券
                  </Text>l
                  <Text
                    className='oneCoupons_title2'>有效期至{moment(item.expiredDate).format('YYYY-MM-DD')}</Text>
                  <Text className='oneCoupons_title3'
                        onClick={clickReceive.bind(this, item.id)}>点击领取</Text>
                </View>
              </View>
            </View>
          ))
        }
      </View>
    </View>
  );
}


export default Index
