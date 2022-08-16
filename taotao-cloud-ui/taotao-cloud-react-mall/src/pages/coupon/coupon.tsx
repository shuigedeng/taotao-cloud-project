import Taro from '@tarojs/taro';
import {Image, ScrollView, Text, View} from '@tarojs/components';
import {get as getGlobalData} from '../../global_data';
import {couponReceive, getCouponListApi} from '../../services/coupon';
import './index.less';
import React, {useEffect, useState} from "react";


const Index: Taro.FC<any> = (props) => {
  // navigationBarTitleText: '优惠券专区'
  const [state, setState] = useState({
    couponList: [],
    page: 1,
    limit: 10,
    count: 0,
    scrollTop: 0,
    showPage: false
  })

  useEffect(() => {
    // 页面渲染完成
    Taro.showToast({
      title: '加载中...',
      icon: 'loading',
      duration: 2000
    });

    getCouponListApi({
      page: state.page,
      limit: state.limit
    }).then(res => {
      setState(prevState => {
        return {
          ...prevState,
          scrollTop: 0,
          couponList: res.list,
          showPage: true,
          count: res.total
        }
      })
      Taro.hideToast();
    }).catch(() => {
      Taro.hideToast();
    })
  }, [state.scrollTop, state.showPage, state.couponList])

  const getCouponList = () => {
    setState(prevState => {
      return {
        ...prevState,
        scrollTop: 0,
        showPage: false,
        couponList: []
      }
    })
  }

  useEffect(() => {
    getCouponList()
  }, [getCouponList, state.page])

  const getCoupon = (e) => {
    if (!getGlobalData('hasLogin')) {
      Taro.navigateTo({
        url: "/pages/auth/login/login"
      });
    }

    let couponId = e.currentTarget.dataset.index;
    couponReceive({
      couponId: couponId
    }).then(() => {
      Taro.showToast({
        title: "领取成功"
      })
    })
  }

  const prevPage = () => {
    if (state.page <= 1) {
      return false;
    }

    setState(prevState => {
      return {...prevState, page: prevState.page - 1}
    })
  }

  const nextPage = () => {
    if (state.page > state.count / state.limit) {
      return true;
    }

    setState(prevState => {
      return {...prevState, page: prevState.page + 1}
    })
  }

  const {scrollTop, couponList, page, showPage, count, limit} = state;

  return (
    <View className='container'>
      <ScrollView className='coupon-list' scrollY scrollTop={scrollTop}>
        {
          couponList.map(item => {
            return <View className='item' key={item.id} onClick={getCoupon} data-index={item.id}>
              <View className='tag'>{item.tag}</View>
              <View className='content'>
                <View className='left'>
                  <View className='discount'>{item.discount}元</View>
                  <View className='min'> 满{item.min}元使用</View>
                </View>
                <View className='right'>
                  <View className='name'>{item.name}</View>
                  {
                    item.days != 0 ? <View className='time'>有效期：{item.days}天</View> :
                      <View className='time'> 有效期：{item.startTime} - {item.endTime}</View>
                  }
                </View>
              </View>
              <View className='condition'>
                <Text className='txt'>{item.desc}</Text>
                <Image src={item.pic} className='icon'/>
              </View>
            </View>
          })
        }
        {
          showPage && <View className='page'>
            <View className={`prev ${page <= 1 ? 'disabled' : ''}`} onClick={prevPage}>上一页</View>
            <View className={`next ${(count / limit) < page ? 'disabled' : ''}`}
                  onClick={nextPage}>下一页</View>
          </View>
        }

      </ScrollView>
    </View>
  );
}

export default Index;
