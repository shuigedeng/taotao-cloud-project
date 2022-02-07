import Taro from '@tarojs/taro';
import {Button, Image, Text, View} from '@tarojs/components';
import * as images from '../../static/images/index';
import './index.less';
import React, {useEffect, useState} from 'react';
import api from "@/api/index"
import {getGlobalData, setGlobalData} from "@/utils/global";

const Index: Taro.FC = () => {
  const [state, setState] = useState({
    userInfo: {
      nickName: '点击登录',
      avatarUrl: '/static/images/my.png'
    },
    order: {
      unpaid: 0,
      unship: 0,
      unrecv: 0,
      uncomment: 0
    },
    hasLogin: false
  })

  useEffect(() => {
    //获取用户的登录信息
    if (getGlobalData('hasLogin')) {
      let userInfo = Taro.getStorageSync('userInfo');
      setState(prevState => {
        return {...prevState, userInfo: userInfo, hasLogin: true}
      })

      api.uc.getLoginUserInfo().then(res => {
        setState(prevState => {
          return {...prevState, order: res.data.order}
        })
      });
    }


  }, [])

  const goLogin = () => {
    if (!state.hasLogin) {
      Taro.navigateTo({
        url: "/pages/auth/login/index"
      });
    }
  }

  const goOrder = () => {
    if (state.hasLogin) {
      try {
        Taro.setStorageSync('tab', 0);
      } catch (e) {

      }
      Taro.navigateTo({
        url: "/pages/ucenter/order/order"
      });
    } else {
      Taro.navigateTo({
        url: "/pages/auth/login/login"
      });
    }
  }

  const goOrderIndex = (e) => {
    // TODO 需处理
    if (state.hasLogin) {
      let tab = e.currentTarget.dataset.index
      let route = e.currentTarget.dataset.route
      try {
        Taro.setStorageSync('tab', tab);
      } catch (e) {

      }
      Taro.navigateTo({
        url: route,
        success: function (res) {
        },
        fail: function (res) {
        },
        complete: function (res) {
        },
      })
    } else {
      Taro.navigateTo({
        url: "/pages/auth/login/login"
      });
    }
  }

  const goAfterSale = () => {
    if (state.hasLogin) {
      Taro.navigateTo({
        url: "/pages/ucenter/aftersaleList/aftersaleList"
      });
    } else {
      Taro.navigateTo({
        url: "/pages/auth/login/login"
      });
    }
  }

  const goCoupon = () => {
    if (state.hasLogin) {
      Taro.navigateTo({
        url: "/pages/ucenter/couponList/couponList"
      });
    } else {
      Taro.navigateTo({
        url: "/pages/auth/login/login"
      });
    }
  }

  const goGroupon = () => {
    if (state.hasLogin) {
      Taro.navigateTo({
        url: "/pages/groupon/myGroupon/myGroupon"
      });
    } else {
      Taro.navigateTo({
        url: "/pages/auth/login/login"
      });
    }
  }

  const goCollect = () => {
    if (state.hasLogin) {
      Taro.navigateTo({
        url: "/pages/ucenter/collect/collect"
      });
    } else {
      Taro.navigateTo({
        url: "/pages/auth/login/login"
      });
    }
  }

  const goFeedback = () => {
    if (state.hasLogin) {
      Taro.navigateTo({
        url: "/pages/ucenter/feedback/feedback"
      });
    } else {
      Taro.navigateTo({
        url: "/pages/auth/login/login"
      });
    }
  }

  const goFootprint = () => {
    if (state.hasLogin) {
      Taro.navigateTo({
        url: "/pages/ucenter/footprint/footprint"
      });
    } else {
      Taro.navigateTo({
        url: "/pages/auth/login/login"
      });
    }
  }

  const goAddress = () => {
    if (state.hasLogin) {
      Taro.navigateTo({
        url: "/pages/ucenter/address/address"
      });
    } else {
      Taro.navigateTo({
        url: "/pages/auth/login/login"
      });
    }
  }

  const bindPhoneNumber = (e) => {
    if (e.detail.errMsg !== "getPhoneNumber:ok") {
      // 拒绝授权
      return;
    }

    if (!state.hasLogin) {
      Taro.showToast({
        title: '绑定失败：请先登录',
        icon: 'none',
        duration: 2000
      });
      return;
    }

    api.uc.bindPhone({
      iv: e.detail.iv,
      encryptedData: e.detail.encryptedData
    }).then(() => {
      Taro.showToast({
        title: '绑定手机号码成功',
        icon: 'success',
        duration: 2000
      });
    })
  }


  const goHelp = () => {
    Taro.navigateTo({
      url: '/pages/help/help'
    });
  }

  const aboutUs = () => {
    Taro.navigateTo({
      url: '/pages/about/about'
    });
  }

  const userLogout = () => {
    Taro.showModal({
      title: '',
      confirmColor: '#b4282d',
      content: '退出登录？',
      success: function (res) {
        if (!res.confirm) {
          return;
        }
        api.uc.logOut().then(() => {
          setGlobalData('hasLogin', false)
          Taro.removeStorageSync('token');
          Taro.removeStorageSync('userInfo');
          Taro.reLaunch({
            url: '/pages/index/index'
          });
        })
      }
    })
  }

  const {userInfo, order, hasLogin} = state;
  return (
    <View className='container'>
      {/*<View className='profile-info' onClick={goLogin}>*/}
      {/*  <Image className='avatar' src={userInfo.avatarUrl}/>*/}
      {/*  <View className='info'>*/}
      {/*    <Text className='name'>{userInfo.nickName}</Text>*/}
      {/*  </View>*/}
      {/*</View>*/}

      {/*<View className='separate'/>*/}

      {/*<View className='user_area'>*/}
      {/*  <View className='user_row' onClick={goOrder}>*/}
      {/*    <View className='user_row_left'>我的订单</View>*/}
      {/*    /!*<van-icon className='user_row_right' name='arrow'/>*!/*/}
      {/*  </View>*/}

      {/*  <View className='user_column'>*/}
      {/*    <View className='user_column_item' onClick={goOrderIndex}*/}
      {/*          data-index='1' data-route='/pages/ucenter/order/order'>*/}
      {/*      {order.unpaid != 0 && <Text*/}
      {/*        className='user_column_item_badge'>{order.unpaid}</Text>}*/}

      {/*      <Image className='user_column_item_image' src={images.pendpay}>*/}
      {/*      </Image>*/}
      {/*      <View className='user_column_item_text'>待付款</View>*/}
      {/*    </View>*/}
      {/*    <View className='user_column_item' onClick={goOrderIndex}*/}
      {/*          data-index='2' data-route='/pages/ucenter/order/order'>*/}
      {/*      {order.unship != 0 && <Text*/}
      {/*        className='user_column_item_badge'>{order.unship}</Text>}*/}
      {/*      <Image className='user_column_item_image'*/}
      {/*             src={images.send}/>*/}
      {/*      <View className='user_column_item_text'>待发货</View>*/}
      {/*    </View>*/}
      {/*    <View className='user_column_item' onClick={goOrderIndex}*/}
      {/*          data-index='3' data-route='/pages/ucenter/order/order'>*/}
      {/*      {order.unrecv != 0 && <Text*/}
      {/*        className='user_column_item_badge'>{order.unrecv}</Text>}*/}

      {/*      <Image className='user_column_item_image'*/}
      {/*             src={images.receive}/>*/}
      {/*      <View className='user_column_item_text'>待收货</View>*/}
      {/*    </View>*/}
      {/*    <View className='user_column_item' onClick={goOrderIndex}*/}
      {/*          data-index='4' data-route='/pages/ucenter/order/order'>*/}
      {/*      {order.uncomment != 0 && <Text*/}
      {/*        className='user_column_item_badge'>{order.uncomment}</Text>}*/}
      {/*      <Image className='user_column_item_image'*/}
      {/*             src={images.comment}/>*/}
      {/*      <View className='user_column_item_text'>待评价</View>*/}
      {/*    </View>*/}
      {/*    <View className='user_column_item' onClick={goAfterSale}>*/}
      {/*      <Image className='user_column_item_image'*/}
      {/*             src={images.aftersale}/>*/}
      {/*      <View className='user_column_item_text'>售后</View>*/}
      {/*    </View>*/}
      {/*  </View>*/}
      {/*</View>*/}

      {/*<View className='separate'/>*/}

      {/*<View className='user_row'>*/}
      {/*  <View className='user_row_left'>核心服务</View>*/}
      {/*</View>*/}
      {/*<View className='user_column'>*/}

      {/*  <View className='user_column_item' onClick={goCoupon}>*/}
      {/*    <Image className='user_column_item_image'*/}
      {/*           src={images.coupon}/>*/}
      {/*    <View className='user_column_item_text'>优惠卷</View>*/}
      {/*  </View>*/}
      {/*  <View className='user_column_item' onClick={goCollect}>*/}
      {/*    <Image className='user_column_item_image'*/}
      {/*           src={images.collect}/>*/}
      {/*    <View className='user_column_item_text'>商品收藏</View>*/}
      {/*  </View>*/}
      {/*  <View className='user_column_item' onClick={goFootprint}>*/}
      {/*    <Image className='user_column_item_image'*/}
      {/*           src={images.footprint}/>*/}
      {/*    <View className='user_column_item_text'>浏览足迹</View>*/}
      {/*  </View>*/}
      {/*  <View className='user_column_item' onClick={goGroupon}>*/}
      {/*    <Image className='user_column_item_image'*/}
      {/*           src={images.group}/>*/}
      {/*    <View className='user_column_item_text'>我的拼团</View>*/}
      {/*  </View>*/}

      {/*  <View className='user_column_item' onClick={goAddress}>*/}
      {/*    <Image className='user_column_item_image'*/}
      {/*           src={images.address}/>*/}
      {/*    <View className='user_column_item_text'>地址管理</View>*/}
      {/*  </View>*/}
      {/*</View>*/}
      {/*<View className='separate'/>*/}

      {/*<View className='user_row'>*/}
      {/*  <View className='user_row_left'>必备工具</View>*/}
      {/*</View>*/}
      {/*<View className='user_column'>*/}

      {/*  <Button className='user_column_item_phone'*/}
      {/*          openType='getPhoneNumber'*/}
      {/*          onGetPhoneNumber={bindPhoneNumber}>*/}
      {/*    <Image className='user_column_item_image'*/}
      {/*           src={images.mobile}/>*/}
      {/*    <View className='user_column_item_text'>绑定手机</View>*/}
      {/*  </Button>*/}
      {/*  <View className='user_column_item' onClick={goHelp}>*/}
      {/*    <Image className='user_column_item_image' src={images.help}/>*/}
      {/*    <View className='user_column_item_text'>帮助中心</View>*/}
      {/*  </View>*/}
      {/*  <View className='user_column_item' onClick={goFeedback}>*/}
      {/*    <Image className='user_column_item_image'*/}
      {/*           src={images.feedback}/>*/}
      {/*    <View className='user_column_item_text'>意见反馈</View>*/}
      {/*  </View>*/}
      {/*  <View className='user_column_item'>*/}
      {/*    /!*<contact-button style='opacity:0;position:absolute;'*!/*/}
      {/*    /!*                type='default-dark' session-from='weapp' size='27'>*!/*/}
      {/*    /!*</contact-button>*!/*/}
      {/*    <Image className='user_column_item_image'*/}
      {/*           src={images.customer}/>*/}
      {/*    <View className='user_column_item_text'>联系客服</View>*/}
      {/*  </View>*/}
      {/*  <View className='user_column_item' onClick={aboutUs}>*/}
      {/*    <Image className='user_column_item_image'*/}
      {/*           src={images.about}/>*/}
      {/*    <View className='user_column_item_text'>关于我们</View>*/}
      {/*  </View>*/}
      {/*</View>*/}
      {/*<View className='separate'/>*/}
      {/*{*/}
      {/*  hasLogin && <View className='logout'*/}
      {/*                    onClick={userLogout}>退出登录</View>*/}
      {/*}*/}

    </View>
  );
}


export default Index;
