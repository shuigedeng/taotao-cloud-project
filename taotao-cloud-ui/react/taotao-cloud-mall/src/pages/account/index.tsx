import Taro, {usePullDownRefresh} from "@tarojs/taro";
import {AtTabs, AtTabsPane} from 'taro-ui'
import {Button, ScrollView, Text, View} from "@tarojs/components";
import "./index.less";
import {withdrawals} from "./service";
import {userInfo} from "../orderDetails/service";
import React, {useEffect, useState} from "react";

interface IState {
  current: number;
  query: {
    loading: boolean
    data: {
      userInfo?: any
    };
  };
  withdrawalsQuery: {
    data?: any;
  }
}


const Index: Taro.FC = () => {
  let [state, setState] = useState<IState>({
    current: 0,
    query: {
      loading: true,
      data: {
        userInfo: {
          balance: 1,
        }
      }
    },
    withdrawalsQuery: {
      data: {
        withdrawals: {
          list: []
        }
      }
    }
  })

  useEffect(() => {
    const init = async () => {
      const userResult = await userInfo();
      setState(prevState => {
        return {...prevState, query: userResult}
      })
      const withdrawalsResult = await withdrawals(1);
      setState(prevState => {
        return {...prevState, withdrawalsQuery: withdrawalsResult}
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

  const handleClick = (value) => {
    setState(prevState => {
      return {...prevState, current: value}
    })
  }

  const forward = () => {
    Taro.navigateTo({
      url: "../withdrawal/index"
    });
  }

  const {query, withdrawalsQuery} = state;
  const withdrawalsList = withdrawalsQuery.data.withdrawals.list;
  return (
    <ScrollView>
      <AtTabs
        animated={false}
        current={state.current}
        tabList={[
          {title: '余额明细'},
          {title: '提现记录'},
        ]}
        onClick={handleClick}>
        <AtTabsPane current={state.current} index={0}>
          <View className="ortherInformationBox">
            <View className="otherInformation">
              <Text className="orderName1">可用余额(元)</Text>
              <Text className="orderName">{query.data.userInfo.balance / 100}</Text>
            </View>
          </View>
          <Button className="button" onClick={forward}>
            前往提现
          </Button>
        </AtTabsPane>
        <AtTabsPane current={state.current} index={1}>
          {withdrawalsList.map(orderItms => (
            <View className="yuy">
              提现记录:
              <View className="yhui">
                <Text className="coupons_text">收款人:</Text>
                <View className="choose_coupons">
                  <Text className='goodName1'>
                    {orderItms.name}
                  </Text>
                </View>
              </View>
              <View className="yhui">
                <Text className="coupons_text">提现卡号:</Text>
                <View className="choose_coupons">
                  <Text className='goodName1'>
                    {orderItms.card}
                  </Text>
                </View>
              </View>
              <View className="yhui">
                <Text className="coupons_text">提现时间:</Text>
                <View className="choose_coupons">
                  <Text className='goodName1'>
                    {orderItms.createdAt}
                  </Text>
                </View>
              </View>
              <View className="yhui">
                <Text className="coupons_text">提现金额:</Text>
                <View className="choose_coupons">
                  <Text className='goodName1'>
                    ￥{orderItms.price / 100}
                  </Text>
                </View>
              </View>
            </View>
          ))}
        </AtTabsPane>
      </AtTabs>
    </ScrollView>
  );
}


export default Index
