import Taro, {usePullDownRefresh} from "@tarojs/taro";
import {Button, Image, OpenData, Text, View} from "@tarojs/components";
import "./index.less";
import {userInfo} from "../orderDetails/service";
import {login} from "../home/service";
import {freightPrice, myCoupons} from "./service";
import {AtIcon, AtModal, AtModalAction, AtModalContent, AtModalHeader} from "taro-ui";
import React, {useEffect, useState} from "react";

interface IState {
  authorization: boolean;
  query: any;
  aboutQuery: {
    data?: any;
  };
  visible: boolean;
  couponsTotal?: any;
}

const Index: Taro.FC = () => {
  let [state, setState] = useState<IState>({
    authorization: false,
    query: null,
    aboutQuery: {
      data: null
    },
    visible: false,
    couponsTotal: null,
  })

  useEffect(() => {
    const init = async () => {
      const token = Taro.getStorageSync('accessToken');
      console.log('token', token);

      if (token) {
        const {data} = await userInfo();
        console.log('data', data);

        setState(prevState => {
          return {...prevState, query: data.userInfo, visible: false}
        })
      }
      const aboutResult = await freightPrice();
      setState(prevState => {
        return {...prevState, aboutQuery: aboutResult}
      })
      const {query} = state;
      if (query === null) {
        Taro.showToast({
          title: '请先登录!',
          icon: 'none'
        });
      }
      const couponsLen = await myCoupons('useable', 1, 1);
      const {pagination} = couponsLen.data.coupons;
      setState(prevState => {
        return {...prevState, couponsTotal: pagination}
      })
    }
    init()
  }, [])

  usePullDownRefresh(async () => {
    const {data} = await userInfo();
    setState(prevState => {
      return {...prevState, query: data.userInfo}
    })
    const couponsLen = await myCoupons('useable', 1, 1);
    const {pagination} = couponsLen.data.coupons;
    setState(prevState => {
      return {...prevState, couponsTotal: pagination}
    })

    setTimeout(() => {
      Taro.stopPullDownRefresh(); //停止下拉刷新
    }, 1000);
  })

  //  绑定手机号
  const handleBind = () => {
    Taro.navigateTo({
      url: "../bindPhoneNum/index"
    });
  }

  // 跳转指定订单
  const handleOrder1 = (id) => {
    Taro.navigateTo({
      url: `../theorder/index?id=${id}`
    });
  }

  //跳转全部订单
  const handleOrderMore = () => {
    Taro.navigateTo({
      url: `../theorder/index?id=0`
    });
  }

  // 授权
  const onGetUserInfo = async (e) => {
    const imageUrl = e.detail.userInfo.avatarUrl;
    const nickname = e.detail.userInfo.nickName;
    const res = await Taro.login();
    const code = res.code;
    const result = await login(code, imageUrl, nickname);
    const token = result.data.accessToken;

    // //将获取到的 token 存入缓存
    Taro.setStorage({
      key: "accessToken",
      data: token
    });
    Taro.showTabBar({});
    Taro.showShareMenu({
      withShareTicket: true
    });

    const setAuthorization = () => {
      setState(prevState => {
        return {...prevState, visible: true}
      })
    };

    //地理位置授权
    await new Promise(resolve => {
      Taro.getLocation({
        type: "wgs84",
        success(res) {
          resolve(res);
        }
      });
    });

    // 检查是否授权了
    Taro.getSetting({
      success(res) {
        if (res.authSetting["scope.userInfo"]) {
          // 已经授权，可以直接调用 getUserInfo 获取头像昵称
          Taro.showTabBar({});
        } else {
          setAuthorization();
          Taro.hideTabBar({});
        }
      }
    });

    const {data} = await userInfo();
    setState(prevState => {
      return {...prevState, query: data.userInfo, visible: false}
    })

    if (!Taro.getStorageSync("storeId")) {
      Taro.navigateTo({
        url: "../nearbystores/index"
      });
    }
  }

  // 余额积分奖励优惠券跳转
  const handleJump = (id) => {
    const {query} = state;
    const userId = query.id;
    switch (id) {
      case 1:
        Taro.navigateTo({
          url: `../../packageA/pages/balance/index?id=${userId}`
        });
        break;
      case 2:
        Taro.navigateTo({
          url: "../../packageA/pages/myCoupons/index"
        });
        break;
      case 3:
        Taro.navigateTo({
          url: `../../packageA/pages/integral/index?id=${userId}`
        });
        break;
      case 4:
        Taro.navigateTo({
          url: "../collection/index"
        });
        break;
      default:
        break;
    }
  }

  //更多服务
  const handle = (id) => {
    switch (id) {
      case 1:
        Taro.chooseAddress({
          async success() {
          }
        });
        break;
      case 4:
        Taro.navigateTo({
          url: `../aboutUs/index`
        });
        break;
      case 5:
        Taro.navigateTo({
          url: "../agreement/index"
        });
        break;
      case 6:
        Taro.navigateTo({
          url: "../../packageA/pages/business/index"
        });
        break;
    }
  }

  const lookForward = () => {
    const {query} = state;
    Taro.checkSession({
      success() {
        if (query.role === "member") {
          Taro.navigateTo({
            url: '../theMemberCenter/index'
          });
        } else {
          Taro.navigateTo({
            url: '../../packageA/pages/topup/index'
          });
        }

      },
      fail() {
        Taro.showToast({
          title: "请登录",
          icon: "none"
        });
      }
    })
  }

  const technicalSupport = () => {
    Taro.navigateToMiniProgram({
      appId: "wx4a96aca05249ba58"
    });
  }

  //暂不授权
  const temporary = () => {
    setState(prevState => {
      return {...prevState, visible: false}
    })
  }

  const OpenLogin = () => {
    setState(prevState => {
      return {...prevState, visible: true}
    })
  }

  const integralExchange = () => {
    Taro.navigateTo({
      url: '../../packageA/pages/activityCoupons/index'
    });
  }

  const {query, visible, couponsTotal} = state;
  console.log('mine query', query);

  const otherInformation = [
    {
      id: 1,
      number: `${query ? (query.balance / 100) : (0)}`,
      name: "余额"
    },
    {
      id: 2,
      number: `${couponsTotal ? (couponsTotal.total) : (0)}`,
      name: "优惠券"
    },
    {
      id: 3,
      number: `${query ? (query.point / 100) : (0)}`,
      name: "积分"
    },
    {
      id: 4,
      number: `${query ? (query.follow) : (0)}`,
      name: "收藏"
    },
  ];
  const orders = [
    {
      id: 0,
      name: '已完成',
      icon: 'https://mengmao-qingying-files.oss-cn-hangzhou.aliyuncs.com/wc.png'
    },
    {
      id: 1,
      name: '已付款',
      icon: 'https://mengmao-qingying-files.oss-cn-hangzhou.aliyuncs.com/fk.png'
    },
    {
      id: 2,
      name: '待取货',
      icon: 'https://mengmao-qingying-files.oss-cn-hangzhou.aliyuncs.com/qh.png'
    },
    {
      id: 3,
      name: '待配送',
      icon: 'https://mengmao-qingying-files.oss-cn-hangzhou.aliyuncs.com/fh.png'
    },
  ];
  const services = [
    {
      id: 1,
      text: "收货地址",
      img: ''
    },
    {
      id: 2,
      text: "在线客服",
      img: ''
    },
    {
      id: 5,
      text: "用户协议",
      img: ''
    },
    {
      id: 6,
      text: "平台资质",
      img: ''
    }
  ];

  return (
    <View className="index">
      <View className='topInformation'>
        <View className='personalTitle'>个人中心</View>
        {/* 上部个人信息 */}
        <View className="userInformation">
          <View className="head">
            <OpenData type="userAvatarUrl"/>
          </View>
          <View className="mine">
            {query ? (
              <View className="mine">
                <OpenData type="userNickName" className="title"/>
                <Text className="phone" onClick={handleBind}>
                  {query.phone ? query.phone : '点击绑定手机'}
                </Text>
              </View>
            ) : (
              <Text className="title2" onClick={OpenLogin}>
                立即登录
              </Text>
            )}
          </View>
          <View className="couponOther">
            <Button
              className="integralExchange"
              onClick={integralExchange}
            >兑换</Button>
          </View>
        </View>

        {/* 余额积分奖励优惠券 */}
        <View className="ortherInformationBox top_padding_bottom">
          {otherInformation.map(item => (
            <View
              className="otherInformation"
              key={item.id}
              onClick={handleJump.bind(this, item.id)}
            >
              <Text className="number">{item.number}</Text>
              <Text className="name">{item.name}</Text>
            </View>
          ))}
        </View>
      </View>
      <View className="huiYuan" onClick={lookForward}>
        <Text className='huiYuanFirst'>Plus会员</Text>
        <Text className='huiYuanSecond'>【专享】领取会员专享优惠券</Text>
        <Text className='huiYuanThird'>立即领取 {">"} </Text>
      </View>
      {/* 我的订单 */}
      <View className="myStoreBox">
        <View className="myOrdersTopLine">
          <Text className="myOrderText">我的订单</Text>
          <View onClick={handleOrderMore}>
            <Text className="textMore">查看更多</Text>
            <AtIcon value='chevron-right' size='20' className='rightArrow'/>
          </View>
        </View>
        <View className="ortherInformationBox">
          {orders.map(order => (
            <View
              className="otherInformation"
              key={order.id}
              onClick={handleOrder1.bind(this, order.id)}
            >
              <Image src={order.icon} className="iconImage"/>
              <Text className="orderName">{order.name}</Text>
            </View>
          ))}
        </View>
      </View>

      {/* 我的服务 */}
      <View className="myServicesBox">
        <View>
          {services.map(service => (
            <View
              key={service.id}
              className={
                service.id !== 2 ? "one-service" : "one-service-other"
              }
              onClick={handle.bind(this, service.id)}
            >
              <View className="service-left-box">
                <Image
                  src={service.img}
                  className={service.id == 1 ? "service-img" : "serviceImg"}
                />
                {service.id !== 2 ? (
                  <Text className="service-left-box-text">
                    {service.text}
                  </Text>
                ) : (
                  <Button open-type="contact" className="button">
                    {service.text}
                  </Button>
                )}
              </View>
              <AtIcon value='chevron-right' className='rightArrow'></AtIcon>
            </View>
          ))}
        </View>
      </View>
      <Text className="support" onClick={technicalSupport}>
        萌猫智科提供技术支持
      </Text>
      {visible ? (
        <AtModal isOpened={visible} closeOnClickOverlay>
          <AtModalHeader>您还未登录</AtModalHeader>
          <AtModalContent>
            <Text className="tit">请先登录再进行操作</Text>
          </AtModalContent>
          <AtModalAction>
            <Button onClick={temporary}>暂不登录</Button>
            <Button
              openType="getUserInfo"
              onGetUserInfo={onGetUserInfo}>立即登录</Button>
          </AtModalAction>
        </AtModal>
      ) : null}
    </View>
  );
}


export default Index
