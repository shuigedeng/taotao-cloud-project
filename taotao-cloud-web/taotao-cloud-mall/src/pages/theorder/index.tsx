import Taro from "@tarojs/taro";
import {Image, Text, View,} from "@tarojs/components";
import {AtTabs, AtTabsPane} from "taro-ui";
import "./index.less";
import {orders} from "./service";
import React, {useEffect, useState} from "react";
import usePullDownRefresh = Taro.usePullDownRefresh;
import useReachBottom = Taro.useReachBottom;
import useRouter = Taro.useRouter;

interface IState {
  current: number;
  list?: any;
  currentPage: number;
  status: string;
  type: string;
}


const Index: Taro.FC = () => {
  let [state, setState] = useState<IState>({
    current: 0,
    list: [],
    currentPage: 1,
    status: 'completed',
    type: 'unmanned',
  })
  const router = useRouter();

  useEffect(() => {
    const init = async () => {
      const {id} = router.params;
      const {currentPage} = state;
      const newCurrent = Number(id);
      setState(prevState => {
        return {...prevState, current: newCurrent}
      })
      let status;
      let type;
      switch (newCurrent) {
        case 0:
          break;
        case 1:
          status = "fetch";
          type = "unmanned";
          break;
        case 2:
          status = "fetch";
          type = "distribution";
          break;
        case 3:
          status = "completed";
          break;
        default:
          break;
      }
      const result = await orders(status, currentPage, type);
      const list = result.data.orders.list;
      setState(prevState => {
        return {...prevState, list}
      })
    }
    init()
  }, [])

  usePullDownRefresh(async () => {
    const {status, type} = state;
    const result = await orders(status, 1, type);
    const list = result.data.orders.list;
    setState(prevState => {
      return {...prevState, list, currentPage: 1,}
    })
    setTimeout(() => {
      Taro.stopPullDownRefresh(); //停止下拉刷新
    }, 1000);
  })

  useReachBottom(async () => {
    const {currentPage, list,} = state;
    const {id} = router.params;
    const newCurrent = Number(id);
    let status;
    let type;
    switch (newCurrent) {
      case 0:
        break;
      case 1:
        status = "fetch";
        type = "unmanned";
        break;
      case 2:
        status = "fetch";
        type = "distribution";
        break;
      case 3:
        status = "completed";
        break;
      default:
        break;
    }
    console.log('status', status);
    console.log('currentPage', currentPage);
    console.log('type', type);

    const currentPageAdd = currentPage + 1;
    const result = await orders(status, currentPageAdd, type);
    console.log('上拉加载result', result);

    const newList = result.data.orders.list;
    console.log('上啦加载newList', newList);

    if (newList.length !== 0) {
      //上拉加载
      Taro.showLoading({
        title: '正在加载',
      })
      setState(prevState => {
        return {...prevState, currentPage: currentPageAdd}
      })
      for (const iterator of newList) {
        console.log('iterator', iterator);
        list.push(iterator);
      }
      setState(prevState => {
        return {...prevState, list}
      })
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
  // tab跳转
  const handleClick = async (value) => {
    const {currentPage} = state;
    setState(prevState => {
      return {...prevState, current: value}
    })
    let statusNow;
    let typeNow;
    switch (value) {
      case 0:
        break;
      case 1:
        statusNow = "fetch";
        typeNow = "unmanned";
        break;
      case 2:
        statusNow = "fetch";
        typeNow = "distribution";
        break;
      case 3:
        statusNow = "completed";
        break;
      default:
        break;
    }
    const result = await orders(statusNow, currentPage, typeNow);
    const list = result.data.orders.list;
    console.log('orders list', list);

    setState(prevState => {
      return {
        ...prevState, status: statusNow,
        type: typeNow,
        list
      }
    })
  }
  const handleOrderInformation = (id) => {
    // let = orderType;
    // if(id===1){

    // }
    Taro.navigateTo({
      url: `../orderInformation/index?id=${id}`
    });
  }

  const {list} = state;
  console.log('list', list);

  const tabList = [
    {title: "全部", id: 0},
    {title: "待取货", id: 1},
    {title: "待配送", id: 2},
    {title: "已完成", id: 3},
  ];
  return (
    <View className="index">
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
            className="atTabs"
          >
            {list.length > 0 ? (
              <View>
                {list.map(order => (
                  <View
                    key={order.id}
                    className="one_order"
                    onClick={() => handleOrderInformation(order.id)}
                  >
                    <View className="one_order_top">
                      <Text className="shop_text">{order.store.name}</Text>
                    </View>
                    {/* <View className="one_order_top_right">
                        <Text className="order_status">订单编号: {order.id}</Text>
                      </View> */}

                    <View className="imgs_box">
                      {order.orderItem.map(orderItms => (
                        <View>
                          {/* <Text className="order_status">
                              {orderItms.title}
                            </Text> */}
                          <View key={orderItms.id} className="oneItemImage">
                            <Image src={orderItms.imageUrl} className="img"/>
                            <Text className="oneItemNumber">
                              ×{orderItms.number}
                            </Text>
                          </View>
                        </View>
                      ))}
                    </View>
                    <View className="one_order_bottom">
                      <Text className="one_order_bottom_priceTotal">
                        实付金额：￥{(order.amount / 100).toFixed(2)}
                      </Text>
                    </View>
                  </View>
                ))}
              </View>
            ) : (
              <View className="nullBox">
                <Image
                  src='https://mengmao-qingying-files.oss-cn-hangzhou.aliyuncs.com/takenGoods.png'
                  className="empty"/>
                <Text className="orederNullText">暂无订单！</Text>
              </View>
            )}
          </AtTabsPane>
        ))}
      </AtTabs>
    </View>
  );
}


export default Index
