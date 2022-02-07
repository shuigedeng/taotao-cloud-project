import Taro, {usePullDownRefresh, useReachBottom, useRouter} from "@tarojs/taro";
import {Button, Text, View} from "@tarojs/components";
import "./index.less";
import {balance} from "./service";

import * as moment from "moment";
import React, {useEffect, useState} from "react";
import {userInfo} from "@/pages/orderDetails/service";

moment.locale("zh-cn");


interface IState {
  current: number;
  myIntegral?: any;
  list?: any;
  currentPage: number;
}

const Index: Taro.FC = () => {
  let [state, setState] = useState<IState>({
    current: 0,
    list: [],
    myIntegral: null,
    currentPage: 1,
  })

  const router = useRouter();

  useEffect(() => {
    const init = async () => {
      const {currentPage} = state;
      const balanceResult = await balance(currentPage);
      const listDetail = balanceResult.data.balance.list;
      setState(prevState => {
        return {...prevState, list: listDetail}
      })

      const token = Taro.getStorageSync('accessToken');
      if (token) {
        const {data} = await userInfo();
        setState(prevState => {
          return {...prevState, myIntegral: data.userInfo}
        })
      }
    }
    init()
  }, [])

  usePullDownRefresh(async () => {
    const balanceResult = await balance(1);
    const listDetail = balanceResult.data.balance.list;
    setState(prevState => {
      return {...prevState, list: listDetail, currentPage: 1}
    })

    setTimeout(() => {
      Taro.stopPullDownRefresh(); //停止下拉刷新
    }, 1000);
  })

  useReachBottom(async () => {
    const {currentPage, list} = state;
    const currentPageAdd = currentPage + 1;
    const balanceResult = await balance(currentPageAdd);
    const {list: newList} = balanceResult.data.items;
    if (newList.length !== 0) {
      //上拉加载
      Taro.showLoading({
        title: '正在加载',
      })
      setState(prevState => {
        return {...prevState, currentPage: currentPageAdd}
      })
      if (newList) {
        for (const iterator of newList) {
          list.push(iterator);
        }
        setState(prevState => {
          return {...prevState, list: list}
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

  const prepaidJump = () => {
    const {id} = router.params;
    Taro.navigateTo({
      url: `../prepaid/index?id=${id}`
    });
  }

  const {list, myIntegral} = state;
  return (
    <View className="index">
      <View className="banner">
      </View>
      <View className="myIntegral">
        <View className="integralCon">
          <View className="integralTxt">
            <Text>当前余额</Text>
            <Text className="integralContent">充值说明</Text>
          </View>
          <View className="integralTxt">
            <Text className="integralTitle">{myIntegral.balance / 100}</Text>元
            <Button
              className="integralExchange"
              onClick={prepaidJump}
            >充值</Button>
          </View>
        </View>
      </View>

      <View className="myIntegralBox">
        <View className="IntegralBoxTitle">
          <Text>余额明细</Text>
          <Text className="IntegralBoxTxt">最近30条余额消费明细</Text>
        </View>
        <View>
          {list.map(item => (
            <View className="IntegralBoxCon">
              <View className="IntegralBoxTime">
                <Text>{item.remark}</Text>
                <Text
                  className="IntegralBoxTimeNum">{moment(item.createdAt).format("YYYY-MM-DD HH:mm:ss")}</Text>
              </View>
              <View className="IntegralBox_view">
                <Text className="IntegralBoxNum">{item.add ? '+' : '-'}</Text>
                <Text className="IntegralBoxNum">{item.price / 100}</Text>
              </View>
            </View>
          ))}
        </View>
      </View>
    </View>
  );
}


export default Index
