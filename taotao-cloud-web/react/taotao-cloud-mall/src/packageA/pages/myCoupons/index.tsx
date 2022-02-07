import Taro, {usePullDownRefresh, useReachBottom} from "@tarojs/taro";
import {Image, Text, View} from "@tarojs/components";
import {AtTabs, AtTabsPane} from "taro-ui";
import "./index.less";
import {myCoupons} from "./service";
import React, {useEffect, useState} from "react";


import * as moment from "moment";

moment.locale("zh-cn");

interface IState {
  current: number;
  pageSize: number;
  currentPage: number;
  couponsList?: any;
  pagination: any;
  status: string;
}

const Index: Taro.FC = () => {
  let [state, setState] = useState<IState>({
    current: 0,
    pageSize: 10,
    currentPage: 1,
    couponsList: [],
    pagination: {},
    status: 'useable',
  })

  useEffect(() => {
    const init = async () => {
      const {status, pageSize, currentPage} = state;
      const {data} = await myCoupons(status, pageSize, currentPage);
      const {list, pagination} = data.coupons;

      setState(prevState => {
        return {...prevState, couponsList: list, pagination: pagination}
      })
    }
    init()
  }, [])

  usePullDownRefresh(async () => {
    const {status, pageSize} = state;
    const {data} = await myCoupons(status, pageSize, 1);
    const {list, pagination} = data.coupons;
    setState(prevState => {
      return {
        ...prevState,
        couponsList: list,
        pagination,
        currentPage: 1,
      }
    })

    setTimeout(() => {
      Taro.stopPullDownRefresh(); //停止下拉刷新
    }, 1000);
  })

  useReachBottom(async () => {
    const {status, pageSize, currentPage, couponsList} = state;
    const currentPageAdd = currentPage + 1;
    const {data} = await myCoupons(status, pageSize, currentPageAdd);
    const {list} = data.coupons;
    if (list.length !== 0) {
      //上拉加载
      Taro.showLoading({
        title: '正在加载',
      })
      setState(prevState => {
        return {
          ...prevState,
          currentPage: currentPageAdd
        }
      })
      if (list) {
        for (const iterator of list) {
          couponsList.push(iterator);
        }
        setState(prevState => {
          return {
            ...prevState,
            couponsList: couponsList
          }
        })
      }
      setTimeout(function () {
        Taro.hideLoading()
      }, 1000)
    } else {
      setTimeout(function () {
        Taro.showToast({
          title: '已全部加载',
          icon: 'success',
          mask: true,
        })
      }, 10)
    }
  })

  //tab跳转
  const handleClick = async (value) => {
    const {pageSize, currentPage} = state;
    let statusNow;
    switch (value) {
      case 0:
        statusNow = "useable";
        break;
      case 1:
        statusNow = "used";
        break;
      case 2:
        statusNow = "expired";
        break;
      default:
        break;
    }
    setState(prevState => {
      return {
        ...prevState,
        current: value,
        status: statusNow
      }
    })
    const {data} = await myCoupons(statusNow, pageSize, currentPage);
    const {list, pagination} = data.coupons;

    setState(prevState => {
      return {
        ...prevState,
        couponsList: list,
        pagination: pagination,
      }
    })
  }

  const {couponsList, pagination} = state;
  console.log('couponsList', couponsList);
  const tabList = [
    {title: "未使用", id: 0},
    {title: "已使用", id: 1},
    {title: "已过期", id: 2},
  ];

  return (
    <View>
      <AtTabs
        current={state.current}
        tabList={tabList}
        onClick={handleClick}
      >
        {tabList.map(item => (
          <AtTabsPane
            current={state.current}
            index={item.id}
            key={item.id}
          >
            {pagination.total !== 0 ? (
              <View>
                {couponsList.map(myCoupon => (
                  <View key={myCoupon.id} className="oneCoupons">
                    {
                      moment(moment(new Date()).format('YYYY-MM-DD')).diff(moment(myCoupon.expiredDate).format('YYYY-MM-DD'), 'day') >= 0 ?
                        <View className='alreadyOverdue'>
                          <Image
                            src='https://mengmao-qingying-files.oss-cn-hangzhou.aliyuncs.com/alreadyUse.png'/>
                        </View>
                        : null
                    }
                    <View className="oneCoupons-left">
                      <Text className="price">￥{myCoupon.amount / 100}</Text>
                    </View>
                    <View className="oneCoupons-right">
                      <View className="oneCoupons-right-top">
                        <View className='oneCoupons-require'>
                          <Text className="fullPrice">
                            满{myCoupon.require / 100}减{myCoupon.amount / 100}
                          </Text>
                        </View>
                        <Text className="type">
                          过期时间:{myCoupon.expiredDate}
                        </Text>
                      </View>
                    </View>
                  </View>
                ))}
              </View>
            ) : (
              <View className="nullBox">
                <Image
                  src='https://mengmao-qingying-files.oss-cn-hangzhou.aliyuncs.com/takenGoods.png'
                  className="empty"/>
                <Text className="orederNullText">暂无可用优惠券！</Text>
              </View>
            )}
          </AtTabsPane>
        ))}
      </AtTabs>
    </View>
  );
}


export default Index

