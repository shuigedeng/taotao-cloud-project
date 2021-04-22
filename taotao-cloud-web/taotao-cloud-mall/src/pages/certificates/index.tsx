import Taro, {usePullDownRefresh} from "@tarojs/taro";
import {Button, Image, OpenData, Swiper, SwiperItem, Text, View} from "@tarojs/components";
import "./index.less";
import {orders} from "./service";
import React, {useEffect, useState} from "react";

interface IState {
  id: {
    data: any;
    loading: boolean
  };
  query: {
    data?: any;
    loading: boolean
  };
  list?: any;
}

const Index: Taro.FC = () => {
  let [state, setState] = useState<IState>({
    id: {
      data: null,
      loading: true,
    },
    query: {
      loading: true,
      data: null
    },
    list: []
  })

  useEffect(() => {
    const paymentId = Taro.getStorageSync("paymentId");
    setState(prevState => {
      return {...prevState, id: paymentId}
    })
  }, [])

  useEffect(() => {
    const init = async () => {
      const paymentId = Taro.getStorageSync("paymentId");
      setState(prevState => {
        return {...prevState, id: paymentId}
      })
      let status;
      status = "fetch";
      let type;
      type = "unmanned";
      const result = await orders(status, 1, type);
      console.log('取货码result', result);

      const list = result.data.orders.list;
      setState(prevState => {
        return {...prevState, query: result, list: list}
      })
    }
    init()
  }, [])

  usePullDownRefresh(async () => {
    const paymentId = Taro.getStorageSync("paymentId");
    setState(prevState => {
      return {...prevState, id: paymentId}
    })
    let status;
    status = "fetch";
    let type;
    type = "unmanned";
    const result = await orders(status, 1, type);
    const list = result.data.orders.list;
    setState(prevState => {
      return {...prevState, query: result, list: list}
    })
    setTimeout(() => {
      Taro.stopPullDownRefresh(); //停止下拉刷新
    }, 1000);
  })


  const goToHome = () => {
    Taro.switchTab({
      url: "../home/index"
    });
  }

  const {list} = state;
  return (
    <View>
      {list.length > 0 ? (
        <View className="index">
          <View className="head">
            <OpenData type="userAvatarUrl"/>
          </View>
          <Swiper
            indicatorColor="#333"
            indicatorActiveColor="#00bc71"
            circular
            indicatorDots
            className="topBanner-box"
          >
            {list.map(order => (
              <SwiperItem>
                <View className="pickUp">
                  <View>
                    <Text className="storeName">请您前往</Text>
                    <Text className="realStoreName">
                      【{order.store.name}】
                    </Text>
                    <Text className="storeName">取货</Text>
                  </View>
                  <View>
                    <View className="ercode">
                      <Image src={order.qrCode} className="ercode1"/>
                    </View>
                    <View className='orther1'>
                      订单编号:<Text className='orther'>{order.id} \n</Text>
                    </View>
                    <View className='orther1'>
                      取货日期:<Text className='orther'> {order.time}</Text>
                    </View>
                    {/* <View className='orther1'>
                        取货时间:<Text className='orther'> {order.time}</Text> */}
                    {/* </View> */}
                  </View>
                </View>
              </SwiperItem>
            ))}

          </Swiper>

        </View>
      ) : (
        <View>
          {/* 分割线 */}
          <View className="coarseLine"/>
          <View className='nullBox'>
            <Image
              src='https://mengmao-qingying-files.oss-cn-hangzhou.aliyuncs.com/takenGoods.png'
              className='empty'
            />
            <Text className='orderNullText'>门店自提订单购买完成后将生成取货二维码</Text>
            <Text className='orderNullText'>凭此二维码前往指定地点取货</Text>
            <Button className='goToHome' onClick={goToHome}>去逛逛</Button>
          </View>
        </View>
      )}
    </View>
  );
}


export default Index
