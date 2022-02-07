import {Image, Text, View} from "@tarojs/components";
import Taro from "@tarojs/taro";
import "./index.less";
import {items} from "./service";

// 引入moment
import moment from "moment";
import React, {useEffect, useState} from "react";
import {useDispatch} from "react-redux";
import useReachBottom = Taro.useReachBottom;
import useRouter = Taro.useRouter;

moment.locale("zh-cn");

interface IState {
  list: any;
  currentPage: number;
  pageSize: number;
}


const Index: Taro.FC = () => {
  let [state, setState] = useState<IState>({
    list: null,
    currentPage: 1,
    pageSize: 10,
  })
  const router = useRouter();
  const dispatch = useDispatch();
  useEffect(() => {
    const init = async () => {
      const {nameLike} = router.params;
      const {currentPage, pageSize} = state;
      const result = await items(nameLike, currentPage, pageSize);
      const {list} = result.data.items;
      setState(prevState => {
        return {...prevState, list}
      })
    }
    init()
  }, [])

  useReachBottom(async () => {
    const {nameLike} = router.params;
    const {currentPage, pageSize, list} = state;
    const currentPageAdd = currentPage + 1;
    const result = await items(nameLike, currentPageAdd, pageSize);
    const {list: newList} = result.data.items;
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
          return {...prevState, list}
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

  /**
   *
   * @param a 商品的 ID
   * @param b 名称
   * @param c 数量
   * @param d 价格
   * @param e 图片
   */
  const onOpenDoor = (code, name, number, price, unit, imageUrl) => {
    const data: any = {};
    data.itemId = code;
    data.name = name;
    data.number = number;
    data.price = price;
    data.unit = unit;
    data.imageUrl = imageUrl;
    dispatch({
      type: 'common/addToCartByCode',
      payload: data
    });
  }

  // 跳转商品详情
  const handleDetails = (code) => {
    Taro.navigateTo({
      url: `../details/index?code=${code}`
    });
  }
  const {list} = state;
  return (
    <View className="index">
      {/* 店铺列表 */}
      <View className="items_box">
        {list.map(item => (
          <View
            className="item_box"
            key={item.code}
          >
            <Image src={item.imageUrl} className="image" onClick={() => handleDetails(item.code)}/>
            <View className="item_bottom_box">
              <Text className="title">{item.name}</Text>
              <Text className="title1">{item.content}</Text>
              <View className="item_right_box">
                <View className="priceBox">
                  <Text className="price">
                    ￥{(item.price / 100).toFixed(2)}/{item.unit}
                  </Text>
                  <Text className="originalPrice">
                    ￥{(item.originalPrice / 100).toFixed(2)}
                  </Text>
                  <Text className="memberPrice">
                    会员价:￥{(item.memberPrice / 100).toFixed(2)}
                  </Text>
                </View>
                <Image
                  src='https://mengmao-qingying-files.oss-cn-hangzhou.aliyuncs.com/shopping_cart.png'
                  className="shoppingCart"
                  onClick={() => onOpenDoor(
                    item.code,
                    item.name,
                    1,
                    item.price,
                    item.unit,
                    item.imageUrl
                  )}
                />
              </View>
            </View>
          </View>
        ))}
      </View>
    </View>
  );
}


export default Index

