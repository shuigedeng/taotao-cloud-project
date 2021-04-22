import Taro from "@tarojs/taro";
import {AtTabs, AtTabsPane} from 'taro-ui'
import {Image, ScrollView, Text, View} from "@tarojs/components";
import "./index.less";
import {balance} from "./service";
import React, {useEffect, useState} from "react";

interface IState {
  current: number;
  current1: number;
  balanceQuery: {
    data: any;
  };
  list?: any;
}

const Index: Taro.FC = () => {
  let [state, setState] = useState<IState>({
    current: 0,
    current1: 0,
    balanceQuery: {
      data: null
    },
    list: []
  })

  useEffect(() => {
    const init = async () => {
      const goods = await balance();
      const loadList = goods.data.balance.list;
      setState(prevState => {
        return {...prevState, balanceQuery: goods, list: loadList}
      })
    }
    init()
  }, [])


  const handleClick = (value) => {
    setState(prevState => {
      return {...prevState, current: value}
    })
  }

  const handle = (value1) => {
    setState(prevState => {
      return {...prevState, current1: value1}
    })
  }

  const {list} = state;

  const labe = [
    {
      id: 1,
      names: 0,
      name: "付款笔数"
    },
    {
      id: 2,
      names: 0.0,
      name: "成交金额"
    },
    {
      id: 2,
      names: 0.0,
      name: "预计收入"
    },

  ];
  return (
    <ScrollView className="index">
      <AtTabs
        animated={false}
        current={state.current}
        tabList={[
          {title: '本月订单'},
          {title: '上月订单'},
        ]}
        onClick={handleClick}>
        <AtTabsPane current={state.current} index={0}>
          <View className="ortherInformationBox">
            {labe.map(order => (
              <View
                className="otherInformation"
                key={order.id}
              >
                <Text className="orderName1">{order.name}</Text>
                <Text className="orderName">{order.names}</Text>
              </View>
            ))}
          </View>
          <AtTabs
            animated={false}
            current={state.current1}
            tabList={[
              {title: '全部'},
              {title: '已付款'},
              {title: '已结算'},
              {title: '已失效'},
            ]}
            onClick={handle}>
            <AtTabsPane current={state.current1} index={0}>
              {list.map(order => (
                <View
                  key={order.id}
                  className="one_order"
                >
                  <View className="one_order_top_right">
                    <Text className="order_status">{order.id}1</Text>
                    <Text className="order_status">{order.balance}2</Text>
                    <Text className="order_status">{order.price}3</Text>
                    <Text className="order_status">{order.add}4</Text>
                    <Text className="order_status">{order.remark}5</Text>
                  </View>

                  <View className="imgs_box">
                    {order.balanceOrder.orderItem.map(orderItms => (
                      <View>
                        <Text className="order_status">
                          {orderItms.title}1
                        </Text>
                        <View key={order.balanceOrder.id} className="oneItemImage">
                          <Image src={orderItms.imageUrl} className="img"/>
                          <Text className="oneItemNumber">
                            ×{orderItms.number}1
                          </Text>
                        </View>
                      </View>
                    ))}
                    {order.balanceStoreInfo.map(orderItms => (
                      <View>
                        <Text className="order_status">
                          {orderItms.name}1
                        </Text>
                        <View key={orderItms.id} className="oneItemImage">
                          1<Image src={orderItms.imageUrl} className="img"/>
                        </View>
                      </View>
                    ))}
                    {order.balanceUser.map(orderItms => (
                      <View>
                        <View key={orderItms.id} className="oneItemImage">1
                        </View>
                      </View>
                    ))}
                  </View>
                  <View className="one_order_bottom">
                    <Text className="one_order_bottom_priceTotal">
                      ￥{(order.balanceOrder.amount / 100).toFixed(2)}
                    </Text>
                  </View>
                </View>
              ))}
            </AtTabsPane>
            <AtTabsPane current={state.current1} index={1} />
            <AtTabsPane current={state.current1} index={2} />
            <AtTabsPane current={state.current1} index={3} />
          </AtTabs>
        </AtTabsPane>
        <AtTabsPane current={state.current} index={1} />
      </AtTabs>
    </ScrollView>
  );
}


export default Index
